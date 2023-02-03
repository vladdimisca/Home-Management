package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.exception.model.ForbiddenException;
import com.amss.homemanagement.model.Comment;
import com.amss.homemanagement.model.Like;
import com.amss.homemanagement.model.User;
import com.amss.homemanagement.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final SecurityService securityService;
    private final CommentService commentService;
    private final TaskService taskService;

    public Like create(Like like, UUID commentId) {
        User user = userService.getById(securityService.getUserId());
        like.setUser(user);
        like.setCreationDate(LocalDateTime.now());
        like.setComment(commentService.getById(commentId));
        return likeRepository.save(like);
    }

    public Like getById(UUID likeId) {
        User user = userService.getById(securityService.getUserId());
        Like like = likeRepository.findById(likeId).orElseThrow(() ->
                new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "like", likeId));
        if (taskService.getFamilyMember(user, like.getComment().getTask().getFamily()).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN); // TODO: Change message
        }
        return like;
    }
}
