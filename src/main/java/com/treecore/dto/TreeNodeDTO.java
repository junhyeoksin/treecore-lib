package com.treecore.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.treecore.domain.TreeNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TreeNode 엔티티의 데이터 전송 객체
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreeNodeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 검증 그룹 정의
    public interface AddNode {}
    public interface UpdateNode {}
    public interface MoveNode {}
    public interface RemoveNode {}
    public interface AlterNode {}
    public interface AlterNodeType {}

    private Long id;
    
    @NotNull(groups = {AddNode.class, MoveNode.class})
    private Long parentId;
    
    private Long position;
    
    @NotBlank(groups = {AddNode.class, UpdateNode.class})
    private String title;
    
    @NotBlank(groups = {AddNode.class})
    private String type;
    
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 트리 구조 관련 필드
    private Long left;
    private Long right;
    private Long level;
    
    // 검색 및 페이징 관련 필드
    @Builder.Default
    private int pageIndex = 1;
    
    @Builder.Default
    private int pageUnit = 10;
    
    @Builder.Default
    private int pageSize = 10;
    
    private String searchCondition;
    private String searchKeyword;
    
    // 상태 관련 필드
    private long status;
    private String message;
    private String state;
    private Long childCount;
    
    // TreeNode 엔티티로부터 DTO 생성하는 생성자
    public TreeNodeDTO(TreeNode entity) {
        this.id = entity.getId();
        this.parentId = entity.getParentId();
        this.position = entity.getPosition();
        this.left = entity.getLeft();
        this.right = entity.getRight();
        this.level = entity.getLevel();
        this.title = entity.getTitle();
        this.type = entity.getType();
        this.description = entity.getDescription();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.state = entity.getState();
        this.childCount = entity.getChildCount();
    }
    
    // 엔티티 변환 메서드
    public TreeNode toEntity() {
        TreeNode entity = new TreeNode();
        entity.setId(this.id);
        entity.setParentId(this.parentId);
        entity.setPosition(this.position);
        entity.setLeft(this.left);
        entity.setRight(this.right);
        entity.setLevel(this.level);
        entity.setTitle(this.title);
        entity.setType(this.type);
        entity.setDescription(this.description);
        entity.setState(this.state);
        entity.setChildCount(this.childCount);
        return entity;
    }
    
    // DTO의 값으로 엔티티를 업데이트
    public void updateEntity(TreeNode entity) {
        if (this.title != null) entity.setTitle(this.title);
        if (this.type != null) entity.setType(this.type);
        if (this.description != null) entity.setDescription(this.description);
        if (this.state != null) entity.setState(this.state);
    }
} 