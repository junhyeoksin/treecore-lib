package com.treecore.service;

import com.treecore.domain.TreeNode;
import com.treecore.dto.PageRequestDTO;
import com.treecore.dto.PageResultDTO;
import com.treecore.dto.TreeNodeDTO;
import com.treecore.repository.TreeNodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * TreeNodeService 인터페이스의 구현 클래스
 */
@Slf4j
@Service
public class TreeNodeServiceImpl implements TreeNodeService {

    private final TreeNodeRepository treeNodeRepository;

    public TreeNodeServiceImpl(TreeNodeRepository treeNodeRepository) {
        this.treeNodeRepository = treeNodeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public TreeNodeDTO getNode(Long nodeId) {
        TreeNode node = treeNodeRepository.findById(nodeId)
                .orElseThrow(() -> new NoSuchElementException("Node not found with id: " + nodeId));
        return new TreeNodeDTO(node);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TreeNodeDTO> getChildNodes(Long parentId) {
        List<TreeNode> childNodes = treeNodeRepository.findByParentIdOrderByPositionDesc(parentId);
        return childNodes.stream()
                .map(TreeNodeDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResultDTO<TreeNodeDTO, TreeNode> getPaginatedChildNodes(Long parentId, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("position").descending());
        Page<TreeNode> result = treeNodeRepository.findByParentId(parentId, pageable);
        return new PageResultDTO<>(result, TreeNodeDTO::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TreeNodeDTO> searchNodes(String keyword) {
        List<TreeNode> nodes = treeNodeRepository.findByTitleContaining(keyword);
        return nodes.stream()
                .map(TreeNodeDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TreeNodeDTO addNode(TreeNodeDTO nodeDTO) {
        // 부모 노드 확인
        TreeNode parentNode = treeNodeRepository.findById(nodeDTO.getParentId())
                .orElseThrow(() -> new NoSuchElementException("Parent node not found with id: " + nodeDTO.getParentId()));

        // 같은 부모 아래의 마지막 위치 계산
        List<TreeNode> siblings = treeNodeRepository.findByParentIdOrderByPositionDesc(nodeDTO.getParentId());
        Long position = siblings.isEmpty() ? 0L : siblings.get(0).getPosition() + 1;

        // 새 노드의 left, right 값 계산
        Long insertPosition = parentNode.getRight() - 1;
        
        // right 값이 insertPosition보다 크거나 같은 노드들의 right 값 증가
        treeNodeRepository.updateRightValuesGreaterThanOrEqual(insertPosition, 2L);
        
        // left 값이 insertPosition보다 큰 노드들의 left 값 증가
        treeNodeRepository.updateLeftValuesGreaterThanOrEqual(insertPosition, 2L);

        // 새 노드 생성
        TreeNode newNode = new TreeNode();
        newNode.setParentId(nodeDTO.getParentId());
        newNode.setPosition(position);
        newNode.setLeft(insertPosition);
        newNode.setRight(insertPosition + 1);
        newNode.setLevel(parentNode.getLevel() + 1);
        newNode.setTitle(nodeDTO.getTitle());
        newNode.setType(nodeDTO.getType());
        newNode.setDescription(nodeDTO.getDescription());

        // 저장 및 반환
        TreeNode savedNode = treeNodeRepository.save(newNode);
        return new TreeNodeDTO(savedNode);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean removeNode(Long nodeId) {
        TreeNode node = treeNodeRepository.findById(nodeId)
                .orElseThrow(() -> new NoSuchElementException("Node not found with id: " + nodeId));

        Long width = node.getRight() - node.getLeft() + 1;

        // 노드와 그 자손들 삭제
        treeNodeRepository.deleteNodeAndDescendants(node.getLeft(), node.getRight());

        // left 값 조정
        treeNodeRepository.updateLeftValuesGreaterThanOrEqual(node.getRight(), -width);

        // right 값 조정
        treeNodeRepository.updateRightValuesGreaterThanOrEqual(node.getRight(), -width);

        return true;
    }

    @Override
    @Transactional
    public TreeNodeDTO updateNode(TreeNodeDTO nodeDTO) {
        TreeNode node = treeNodeRepository.findById(nodeDTO.getId())
                .orElseThrow(() -> new NoSuchElementException("Node not found with id: " + nodeDTO.getId()));

        // 업데이트할 필드만 변경
        nodeDTO.updateEntity(node);

        // 저장 및 반환
        TreeNode updatedNode = treeNodeRepository.save(node);
        return new TreeNodeDTO(updatedNode);
    }

    @Override
    @Transactional
    public TreeNodeDTO updateNodeType(Long id, String type) {
        TreeNode node = treeNodeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Node not found with id: " + id));
        
        node.setType(type);
        
        TreeNode updatedNode = treeNodeRepository.save(node);
        
        return new TreeNodeDTO(updatedNode);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TreeNodeDTO moveNode(Long nodeId, Long newParentId, Long newPosition) {
        TreeNode node = treeNodeRepository.findById(nodeId)
                .orElseThrow(() -> new NoSuchElementException("Node not found with id: " + nodeId));
        
        TreeNode newParent = treeNodeRepository.findById(newParentId)
                .orElseThrow(() -> new NoSuchElementException("New parent node not found with id: " + newParentId));

        // 현재 노드의 너비 계산
        Long width = node.getRight() - node.getLeft() + 1;
        
        // 임시로 음수 값으로 변경하여 다른 노드들과 충돌 방지
        treeNodeRepository.updateLeftValuesGreaterThanOrEqual(node.getLeft(), -1000L);
        treeNodeRepository.updateRightValuesGreaterThanOrEqual(node.getLeft(), -1000L);
        
        // 이동할 위치 계산
        Long insertPosition = newParent.getRight() - 1;
        
        // right 값 조정
        treeNodeRepository.updateRightValuesGreaterThanOrEqual(insertPosition, width);
        
        // left 값 조정
        treeNodeRepository.updateLeftValuesGreaterThanOrEqual(insertPosition, width);
        
        // 노드와 그 자손들의 위치 조정
        Long levelDiff = newParent.getLevel() + 1 - node.getLevel();
        Long leftDiff = insertPosition - node.getLeft();
        
        // 노드 업데이트
        node.setParentId(newParentId);
        node.setPosition(newPosition);
        node.setLeft(node.getLeft() + leftDiff + 1000L);
        node.setRight(node.getRight() + leftDiff + 1000L);
        node.setLevel(node.getLevel() + levelDiff);
        
        // 저장 및 반환
        TreeNode movedNode = treeNodeRepository.save(node);
        return new TreeNodeDTO(movedNode);
    }

    @Override
    @Transactional
    public TreeNodeDTO createRootNode(String title, String type) {
        TreeNode rootNode = TreeNode.createRoot(title, type);
        TreeNode savedNode = treeNodeRepository.save(rootNode);
        
        return new TreeNodeDTO(savedNode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TreeNodeDTO> getTree() {
        List<TreeNode> rootNodes = treeNodeRepository.findByParentIdOrderByPositionDesc(0L);
        return rootNodes.stream()
                .map(TreeNodeDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TreeNodeDTO> getAncestors(Long nodeId) {
        TreeNode node = treeNodeRepository.findById(nodeId)
                .orElseThrow(() -> new NoSuchElementException("Node not found with id: " + nodeId));
        
        List<TreeNode> ancestors = treeNodeRepository.findAll().stream()
                .filter(n -> n.getLeft() < node.getLeft() && n.getRight() > node.getRight())
                .sorted((a, b) -> a.getLevel().compareTo(b.getLevel()))
                .collect(Collectors.toList());
        
        return ancestors.stream()
                .map(TreeNodeDTO::new)
                .collect(Collectors.toList());
    }
} 