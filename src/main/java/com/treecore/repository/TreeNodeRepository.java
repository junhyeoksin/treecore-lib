package com.treecore.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.treecore.domain.TreeNode;

/**
 * TreeNode 엔티티에 대한 데이터 액세스 인터페이스
 */
@Repository
public interface TreeNodeRepository extends JpaRepository<TreeNode, Long> {

    /**
     * 부모 ID로 자식 노드들을 조회하고 위치 순으로 정렬
     * @param parentId 부모 노드 ID
     * @return 자식 노드 목록
     */
    List<TreeNode> findByParentIdOrderByPositionDesc(Long parentId);
    
    // 부모 ID로 자식 노드 페이징 조회
    Page<TreeNode> findByParentId(Long parentId, Pageable pageable);
    
    // 제목으로 노드 검색
    List<TreeNode> findByTitleContaining(String title);
    
    // 특정 노드의 모든 자손 조회 (left, right 값 사용)
    @Query("SELECT t FROM TreeNode t WHERE t.left > :left AND t.right < :right ORDER BY t.left")
    List<TreeNode> findDescendants(@Param("left") Long left, @Param("right") Long right);
    
    // 특정 노드의 모든 조상 조회 (left, right 값 사용)
    @Query("SELECT t FROM TreeNode t WHERE t.left < :left AND t.right > :right ORDER BY t.level")
    List<TreeNode> findAncestors(@Param("left") Long left, @Param("right") Long right);
    
    /**
     * 특정 노드와 그 하위 노드들을 삭제
     * @param left 노드의 left 값
     * @param right 노드의 right 값
     */
    @Modifying
    @Query("DELETE FROM TreeNode t WHERE t.left >= :left AND t.right <= :right")
    void deleteNodeAndDescendants(@Param("left") Long left, @Param("right") Long right);
    
    // 특정 값보다 큰 left 값을 가진 노드들의 left 값을 조정
    @Modifying
    @Query("UPDATE TreeNode t SET t.left = t.left + :offset WHERE t.left >= :threshold")
    void updateLeftValuesGreaterThanOrEqual(@Param("threshold") Long threshold, @Param("offset") Long offset);
    
    // 특정 값보다 큰 right 값을 가진 노드들의 right 값을 조정
    @Modifying
    @Query("UPDATE TreeNode t SET t.right = t.right + :offset WHERE t.right >= :threshold")
    void updateRightValuesGreaterThanOrEqual(@Param("threshold") Long threshold, @Param("offset") Long offset);
    
    // 특정 부모 노드 아래의 최대 position 값을 조회
    @Query("SELECT MAX(t.position) FROM TreeNode t WHERE t.parentId = :parentId")
    Long findMaxPositionByParentId(@Param("parentId") Long parentId);
    
    /**
     * 특정 범위 내의 노드들을 조회
     * @param left 시작 left 값
     * @param right 종료 right 값
     * @return 범위 내 노드 목록
     */
    @Query("SELECT t FROM TreeNode t WHERE t.left >= :left AND t.right <= :right ORDER BY t.left")
    List<TreeNode> findNodesInRange(@Param("left") Long left, @Param("right") Long right);
} 