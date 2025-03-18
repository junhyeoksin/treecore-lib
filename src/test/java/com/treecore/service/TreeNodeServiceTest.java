package com.treecore.service;

import com.treecore.domain.TreeNode;
import com.treecore.dto.TreeNodeDTO;
import com.treecore.repository.TreeNodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * TreeNodeService 단위 테스트
 * 이 테스트는 TreeNodeService의 기능을 검증합니다.
 * 실제 데이터베이스 연결 없이 Mock 객체를 사용하여 테스트합니다.
 */
@ExtendWith(MockitoExtension.class)
public class TreeNodeServiceTest {

    @Mock
    private TreeNodeRepository treeNodeRepository;

    @InjectMocks
    private TreeNodeServiceImpl treeNodeService;

    private TreeNode rootNode;
    private TreeNode childNode;

    @BeforeEach
    void setUp() {
        // 테스트용 TreeNode 객체 생성
        rootNode = new TreeNode();
        rootNode.setId(1L);
        rootNode.setParentId(0L);
        rootNode.setPosition(0L);
        rootNode.setLeft(1L);
        rootNode.setRight(4L);
        rootNode.setLevel(0L);
        rootNode.setTitle("Root");
        rootNode.setType("folder");

        childNode = new TreeNode();
        childNode.setId(2L);
        childNode.setParentId(1L);
        childNode.setPosition(0L);
        childNode.setLeft(2L);
        childNode.setRight(3L);
        childNode.setLevel(1L);
        childNode.setTitle("Child");
        childNode.setType("item");
    }

    @Test
    @DisplayName("노드 조회 테스트")
    void testGetNode() {
        // given
        when(treeNodeRepository.findById(1L)).thenReturn(Optional.of(rootNode));

        // when
        TreeNodeDTO result = treeNodeService.getNode(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Root");
    }

    @Test
    @DisplayName("자식 노드 조회 테스트")
    void testGetChildNodes() {
        // given
        when(treeNodeRepository.findByParentIdOrderByPositionDesc(1L)).thenReturn(Arrays.asList(childNode));

        // when
        List<TreeNodeDTO> result = treeNodeService.getChildNodes(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result.get(0).getParentId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("노드 추가 테스트")
    void testAddNode() {
        // given
        TreeNodeDTO newNodeDTO = new TreeNodeDTO();
        newNodeDTO.setParentId(1L);
        newNodeDTO.setTitle("New Node");
        newNodeDTO.setType("item");

        TreeNode savedNode = new TreeNode();
        savedNode.setId(3L);
        savedNode.setParentId(1L);
        savedNode.setPosition(1L);
        savedNode.setLeft(4L);
        savedNode.setRight(5L);
        savedNode.setLevel(1L);
        savedNode.setTitle("New Node");
        savedNode.setType("item");

        when(treeNodeRepository.findById(1L)).thenReturn(Optional.of(rootNode));
        when(treeNodeRepository.findByParentIdOrderByPositionDesc(1L)).thenReturn(Arrays.asList(childNode));
        when(treeNodeRepository.save(any(TreeNode.class))).thenReturn(savedNode);

        // when
        TreeNodeDTO result = treeNodeService.addNode(newNodeDTO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getTitle()).isEqualTo("New Node");
        
        verify(treeNodeRepository).updateRightValuesGreaterThanOrEqual(anyLong(), anyLong());
        verify(treeNodeRepository).updateLeftValuesGreaterThanOrEqual(anyLong(), anyLong());
    }

    @Test
    @DisplayName("노드 삭제 테스트")
    void testRemoveNode() {
        // given
        when(treeNodeRepository.findById(2L)).thenReturn(Optional.of(childNode));

        // when
        boolean result = treeNodeService.removeNode(2L);

        // then
        assertThat(result).isTrue();
        
        verify(treeNodeRepository).deleteNodeAndDescendants(anyLong(), anyLong());
        verify(treeNodeRepository).updateLeftValuesGreaterThanOrEqual(anyLong(), anyLong());
        verify(treeNodeRepository).updateRightValuesGreaterThanOrEqual(anyLong(), anyLong());
    }

    @Test
    @DisplayName("노드 업데이트 테스트")
    void testUpdateNode() {
        // given
        TreeNodeDTO updateDTO = new TreeNodeDTO();
        updateDTO.setId(1L);
        updateDTO.setTitle("Updated Root");

        TreeNode updatedNode = new TreeNode();
        updatedNode.setId(1L);
        updatedNode.setParentId(0L);
        updatedNode.setPosition(0L);
        updatedNode.setLeft(1L);
        updatedNode.setRight(4L);
        updatedNode.setLevel(0L);
        updatedNode.setTitle("Updated Root");
        updatedNode.setType("folder");

        when(treeNodeRepository.findById(1L)).thenReturn(Optional.of(rootNode));
        when(treeNodeRepository.save(any(TreeNode.class))).thenReturn(updatedNode);

        // when
        TreeNodeDTO result = treeNodeService.updateNode(updateDTO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Updated Root");
    }
} 