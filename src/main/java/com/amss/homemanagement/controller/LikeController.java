package com.amss.homemanagement.controller;

import com.amss.homemanagement.dto.LikeDto;
import com.amss.homemanagement.mapper.LikeMapper;
import com.amss.homemanagement.model.Like;
import com.amss.homemanagement.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final LikeMapper likeMapper;

    @PostMapping
    public ResponseEntity<LikeDto> create(@Valid @RequestBody LikeDto likeDto) {
        Like like = likeService.create(likeMapper.mapToEntity(likeDto), likeDto.commentId());
        return ResponseEntity
                .created(URI.create("/api/likes/" + like.getId()))
                .body(likeMapper.mapToDto(like));
    }

    @GetMapping("/{likeId}")
    public ResponseEntity<LikeDto> getById(@PathVariable(name = "likeId") UUID likeId) {
        Like like = likeService.getById(likeId);
        return ResponseEntity.ok(likeMapper.mapToDto(like));
    }
}
