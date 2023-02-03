package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.exception.model.ConflictException;
import com.amss.homemanagement.exception.model.ForbiddenException;
import com.amss.homemanagement.model.Comment;
import com.amss.homemanagement.model.Family;
import com.amss.homemanagement.model.FamilyMember;
import com.amss.homemanagement.model.User;
import com.amss.homemanagement.repository.CommentRepository;
import com.amss.homemanagement.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final SecurityService securityService;

    public Comment create(Comment comment, UUID familyId, UUID taskId) {
        User user = userService.getById(securityService.getUserId());
        // verifica ca exista task-ul cu metoda din task service, to do
        comment.setUser(user);
        comment.setCreationDate(LocalDateTime.now());
        comment.setTaskId(taskId);
        return commentRepository.save(comment);
    }

    public Comment getById(UUID familyId, UUID taskId, UUID commentId) {
        // verifica ca exista task-ul cu metoda din task service, to do
        return commentRepository.findById(commentId).orElseThrow(() ->
                new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "comment", commentId));
    }

    public Comment updateById(UUID familyId, UUID taskId, UUID commentId, Comment comment) {
        // verifica ca exista task-ul cu metoda din task service, to do
        Comment existingComment = getById(familyId, taskId, commentId);
        securityService.authorize(existingComment.getUser().getId());

        existingComment.setContent(comment.getContent());
        return commentRepository.save(existingComment);
    }

    public void deleteById(UUID familyId, UUID taskId, UUID commentId) {
        // verifica ca exista task-ul cu metoda din task service, to do
        Comment existingComment = getById(familyId, taskId, commentId);
        securityService.authorize(existingComment.getUser().getId());

        commentRepository.delete(existingComment);
    }

    public List<Comment> getByTaskId(UUID familyId, UUID taskId) {
        // verifica ca exista task-ul cu metoda din task service, to do
        return commentRepository.findCommentsByTaskId(taskId);
    }

}
