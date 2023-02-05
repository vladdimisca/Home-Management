package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.exception.model.ForbiddenException;
import com.amss.homemanagement.model.Comment;
import com.amss.homemanagement.model.Task;
import com.amss.homemanagement.model.User;
import com.amss.homemanagement.repository.CommentRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final SecurityService securityService;
    private final TaskService taskService;
    private final FamilyService familyService;

    public Comment create(Comment comment, @NotNull UUID taskId) {
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
        if (familyService.getFamilyMember(user, comment.getTask().getFamily()).isEmpty()) {
            throw new ExceptionFactory().createException(HttpStatus.FORBIDDEN, ErrorMessage.NOT_PART_OF_FAMILY);
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
        User user = userService.getById(securityService.getUserId());
        Task task = taskService.getById(taskId);
        if (familyService.getFamilyMember(user, task.getFamily()).isEmpty()) {
            throw new ExceptionFactory().createException(HttpStatus.FORBIDDEN, ErrorMessage.NOT_PART_OF_FAMILY);
        }
        return commentRepository.findCommentsByTaskId(taskId);
    }

}
