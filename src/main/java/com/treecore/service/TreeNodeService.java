package com.treecore.service;

import java.util.List;

import com.treecore.dto.PageRequestDTO;
import com.treecore.dto.PageResultDTO;
import com.treecore.dto.TreeNodeDTO;

/**
 * 트리 노드 관리를 위한 서비스 인터페이스
 */
public interface TreeNodeService {

    /**
     * 특정 ID의 노드를 조회
     * @param nodeId 노드 ID
     * @return 노드 DTO
     */
    TreeNodeDTO getNode(Long nodeId);

    /**
     * 특정 부모 노드의 자식 노드들을 조회
     * @param parentId 부모 노드 ID
     * @return 자식 노드 DTO 목록
     */
    List<TreeNodeDTO> getChildNodes(Long parentId);

    /**
     * 페이징된 자식 노드 조회
     * @param parentId 부모 노드 ID
     * @param pageRequestDTO 페이징 요청 DTO
     * @return 페이징된 자식 노드 DTO 목록
     */
    PageResultDTO<TreeNodeDTO, ?> getPaginatedChildNodes(Long parentId, PageRequestDTO pageRequestDTO);

    /**
     * 노드 검색
     * @param keyword 검색 키워드
     * @return 검색된 노드 DTO 목록
     */
    List<TreeNodeDTO> searchNodes(String keyword);

    /**
     * 새로운 노드를 추가
     * @param nodeDTO 추가할 노드 정보
     * @return 추가된 노드 DTO
     */
    TreeNodeDTO addNode(TreeNodeDTO nodeDTO);

    /**
     * 노드를 삭제
     * @param nodeId 삭제할 노드 ID
     * @return 삭제 성공 여부
     */
    boolean removeNode(Long nodeId);

    /**
     * 노드 정보를 업데이트
     * @param nodeDTO 업데이트할 노드 정보
     * @return 업데이트된 노드 DTO
     */
    TreeNodeDTO updateNode(TreeNodeDTO nodeDTO);

    /**
     * 노드 타입 변경
     * @param id 노드 ID
     * @param type 새로운 타입
     * @return 변경된 노드 DTO
     */
    TreeNodeDTO updateNodeType(Long id, String type);

    /**
     * 노드를 이동
     * @param nodeId 이동할 노드 ID
     * @param newParentId 새 부모 노드 ID
     * @param newPosition 새 위치
     * @return 이동된 노드 DTO
     */
    TreeNodeDTO moveNode(Long nodeId, Long newParentId, Long newPosition);

    /**
     * 전체 트리 구조를 조회
     * @return 트리 구조의 루트 노드부터 시작하는 노드 DTO 목록
     */
    List<TreeNodeDTO> getTree();

    /**
     * 특정 노드의 조상 노드들을 조회
     * @param nodeId 노드 ID
     * @return 조상 노드 DTO 목록
     */
    List<TreeNodeDTO> getAncestors(Long nodeId);

    /**
     * 루트 노드를 생성
     * @param title 노드 제목
     * @param type 노드 타입
     * @return 생성된 루트 노드 DTO
     */
    TreeNodeDTO createRootNode(String title, String type);
} 