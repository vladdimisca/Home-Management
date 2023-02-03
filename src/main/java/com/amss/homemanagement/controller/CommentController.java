package com.amss.homemanagement.controller;

import com.amss.homemanagement.dto.CommentDto;
import com.amss.homemanagement.dto.FamilyDto;
import com.amss.homemanagement.mapper.CommentMapper;
import com.amss.homemanagement.model.Comment;
import com.amss.homemanagement.model.Family;
import com.amss.homemanagement.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/families/{familyId}/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping
    public ResponseEntity<CommentDto> create(@Valid @RequestBody CommentDto commentDto,
                                             @PathVariable(name = "familyId") UUID familyId,
                                             @PathVariable(name = "taskId") UUID taskId) {
        Comment comment = commentService.create(commentMapper.mapToEntity(commentDto), familyId, taskId);
        return ResponseEntity
                .created(URI.create("/api/family/" + familyId + "tasks/" + taskId + "/comments/" + comment.getId()))
                .body(commentMapper.mapToDto(comment));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getById(@PathVariable(name = "familyId") UUID familyId,
                                              @PathVariable(name = "taskId") UUID taskId,
                                              @PathVariable(name = "commentId") UUID commentId) {
        Comment comment = commentService.getById(familyId, taskId, commentId);
        return ResponseEntity.ok(commentMapper.mapToDto(comment));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> update(@PathVariable(name = "familyId") UUID familyId,
                                             @PathVariable(name = "taskId") UUID taskId,
                                             @PathVariable(name = "commentId") UUID commentId,
                                             @Valid @RequestBody CommentDto commentDto) {
        Comment comment = commentService.updateById(familyId, taskId, commentId, commentMapper.mapToEntity(commentDto));
        return ResponseEntity.ok(commentMapper.mapToDto(comment));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> delete(@PathVariable(name = "familyId") UUID familyId,
                                    @PathVariable(name = "taskId") UUID taskId,
                                    @PathVariable(name = "commentId") UUID commentId) {
        commentService.deleteById(familyId, taskId, commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getByTaskId(@PathVariable(name = "familyId") UUID familyId,
                                                        @PathVariable(name = "taskId") UUID taskId) {
        List<Comment> comments = commentService.getByTaskId(familyId, taskId);
        return ResponseEntity.ok(comments.stream().map(commentMapper::mapToDto).toList());
    }
}
