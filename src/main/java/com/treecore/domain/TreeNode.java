package com.treecore.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

/**
 * 트리 구조의 노드를 표현하는 엔티티 클래스
 */
@Entity
@Table(name = "tree_nodes")
public class TreeNode implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "parent_id", nullable = false)
    private Long parentId;
    
    @Column(nullable = false)
    private Long position;
    
    @Column(name = "left_value", nullable = false)
    private Long left;
    
    @Column(name = "right_value", nullable = false)
    private Long right;
    
    @Column(nullable = false)
    private Long level;
    
    @Column(nullable = false, length = 255)
    private String title;
    
    @Column(nullable = false, length = 50)
    private String type;
    
    // 추가 필드들
    @Column(length = 1000)
    private String description;
    
    @Column(length = 50)
    private String state;
    
    @Column(name = "child_count")
    private Long childCount;
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
    
    // 기본 생성자
    public TreeNode() {
        this.childCount = 0L;
    }
    
    // 모든 필드를 포함한 생성자
    public TreeNode(Long id, Long parentId, Long position, Long left, Long right, Long level, String title, String type) {
        this.id = id;
        this.parentId = parentId;
        this.position = position;
        this.left = left;
        this.right = right;
        this.level = level;
        this.title = title;
        this.type = type;
        this.childCount = 0L;
    }
    
    // 루트 노드 생성 메서드
    public static TreeNode createRoot(String title, String type) {
        TreeNode rootNode = new TreeNode();
        rootNode.setParentId(0L);
        rootNode.setPosition(0L);
        rootNode.setLeft(1L);
        rootNode.setRight(2L);
        rootNode.setLevel(0L);
        rootNode.setTitle(title);
        rootNode.setType(type);
        rootNode.setState("active");
        rootNode.setChildCount(0L);
        return rootNode;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = createdAt;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public Long getPosition() {
        return position;
    }
    
    public void setPosition(Long position) {
        this.position = position;
    }
    
    public Long getLeft() {
        return left;
    }
    
    public void setLeft(Long left) {
        this.left = left;
    }
    
    public Long getRight() {
        return right;
    }
    
    public void setRight(Long right) {
        this.right = right;
    }
    
    public Long getLevel() {
        return level;
    }
    
    public void setLevel(Long level) {
        this.level = level;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public java.time.LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public Long getChildCount() {
        return childCount;
    }
    
    public void setChildCount(Long childCount) {
        this.childCount = childCount;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode treeNode = (TreeNode) o;
        return id != null && id.equals(treeNode.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "TreeNode{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", position=" + position +
                ", left=" + left +
                ", right=" + right +
                ", level=" + level +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
} 