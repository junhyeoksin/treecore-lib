package com.treecore.controller;

import com.treecore.dto.PageRequestDTO;
import com.treecore.dto.PageResultDTO;
import com.treecore.dto.TreeNodeDTO;
import com.treecore.service.TreeNodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/nodes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TreeNodeController {

    private final TreeNodeService treeNodeService;

    @GetMapping("/{id}")
    public ResponseEntity<TreeNodeDTO> getNode(@PathVariable Long id) {
        TreeNodeDTO node = treeNodeService.getNode(id);
        return ResponseEntity.ok(node);
    }

    @GetMapping("/children/{parentId}")
    public ResponseEntity<List<TreeNodeDTO>> getChildNodes(@PathVariable Long parentId) {
        List<TreeNodeDTO> children = treeNodeService.getChildNodes(parentId);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/tree")
    public ResponseEntity<List<TreeNodeDTO>> getTree() {
        List<TreeNodeDTO> tree = treeNodeService.getTree();
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/children/paging/{parentId}")
    public ResponseEntity<PageResultDTO<TreeNodeDTO, ?>> getPaginatedChildNodes(
            @PathVariable Long parentId,
            @ModelAttribute PageRequestDTO pageRequestDTO) {
        PageResultDTO<TreeNodeDTO, ?> result = treeNodeService.getPaginatedChildNodes(parentId, pageRequestDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TreeNodeDTO>> searchNodes(@RequestParam String keyword) {
        List<TreeNodeDTO> nodes = treeNodeService.searchNodes(keyword);
        return ResponseEntity.ok(nodes);
    }

    @PostMapping
    public ResponseEntity<TreeNodeDTO> addNode(@RequestBody TreeNodeDTO nodeDTO) {
        TreeNodeDTO addedNode = treeNodeService.addNode(nodeDTO);
        return ResponseEntity.ok(addedNode);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> removeNode(@PathVariable Long id) {
        boolean result = treeNodeService.removeNode(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreeNodeDTO> updateNode(
            @PathVariable Long id,
            @RequestBody TreeNodeDTO nodeDTO) {
        nodeDTO.setId(id);
        TreeNodeDTO updatedNode = treeNodeService.updateNode(nodeDTO);
        return ResponseEntity.ok(updatedNode);
    }

    @PatchMapping("/{id}/type")
    public ResponseEntity<TreeNodeDTO> updateNodeType(
            @PathVariable Long id,
            @RequestBody TreeNodeDTO nodeDTO) {
        TreeNodeDTO updatedNode = treeNodeService.updateNodeType(id, nodeDTO.getType());
        return ResponseEntity.ok(updatedNode);
    }

    @PatchMapping("/{id}/move")
    public ResponseEntity<TreeNodeDTO> moveNode(
            @PathVariable Long id,
            @RequestBody TreeNodeDTO nodeDTO) {
        TreeNodeDTO movedNode = treeNodeService.moveNode(id, nodeDTO.getParentId(), nodeDTO.getPosition());
        return ResponseEntity.ok(movedNode);
    }

    @PostMapping("/root")
    public ResponseEntity<TreeNodeDTO> createRootNode(
            @RequestParam String title,
            @RequestParam String type) {
        TreeNodeDTO rootNode = treeNodeService.createRootNode(title, type);
        return ResponseEntity.ok(rootNode);
    }
} 