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
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping
    public ResponseEntity<CommentDto> create(@Valid @RequestBody CommentDto commentDto) {
        Comment comment = commentService.create(commentMapper.mapToEntity(commentDto), commentDto.taskId());
        return ResponseEntity
                .created(URI.create("/api/comments/" + comment.getId()))
                .body(commentMapper.mapToDto(comment));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getById(@PathVariable(name = "commentId") UUID commentId) {
        Comment comment = commentService.getById(commentId);
        return ResponseEntity.ok(commentMapper.mapToDto(comment));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> update(@PathVariable(name = "commentId") UUID commentId,
                                             @Valid @RequestBody CommentDto commentDto) {
        Comment comment = commentService.updateById(commentId, commentMapper.mapToEntity(commentDto));
        return ResponseEntity.ok(commentMapper.mapToDto(comment));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> delete(@PathVariable(name = "commentId") UUID commentId) {
        commentService.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentDto>> getByTaskId(@PathVariable(name = "taskId") UUID taskId) {
        List<Comment> comments = commentService.getByTaskId(taskId);
        return ResponseEntity.ok(comments.stream().map(commentMapper::mapToDto).toList());
    }
}
