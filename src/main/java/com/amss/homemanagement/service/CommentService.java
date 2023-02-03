package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.exception.model.ForbiddenException;
import com.amss.homemanagement.model.Comment;
import com.amss.homemanagement.model.User;
import com.amss.homemanagement.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final SecurityService securityService;
    private final TaskService taskService;

    public Comment create(Comment comment, UUID taskId) {
        User user = userService.getById(securityService.getUserId());
        comment.setUser(user);
        comment.setCreationDate(LocalDateTime.now());
        comment.setTask(taskService.getById(taskId));
        return commentRepository.save(comment);
    }

    public Comment getById(UUID commentId) {
        User user = userService.getById(securityService.getUserId());
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "comment", commentId));
        if (taskService.getFamilyMember(user, comment.getTask().getFamily()).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN); // TODO: Change message
        }
        return comment;
    }

    public Comment updateById(UUID commentId, Comment comment) {
        Comment existingComment = getById(commentId);
        securityService.authorize(existingComment.getUser().getId());

        existingComment.setContent(comment.getContent());
        return commentRepository.save(existingComment);
    }

    public void deleteById(UUID commentId) {
        Comment existingComment = getById(commentId);
        securityService.authorize(existingComment.getUser().getId());

        commentRepository.delete(existingComment);
    }

    public List<Comment> getByTaskId(UUID taskId) {
        taskService.getById(taskId);
        return commentRepository.findCommentsByTaskId(taskId);
    }

}
