package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.exception.model.ForbiddenException;
import com.amss.homemanagement.model.Notification;
import com.amss.homemanagement.model.Task;
import com.amss.homemanagement.model.User;
import com.amss.homemanagement.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailSenderService emailSenderService;
    private final UserService userService;
    private final SecurityService securityService;
    private final FamilyService familyService;

    @Transactional
    public Notification create(User assignee, Task task) {
        Notification notification = new Notification();
        notification.setDate(LocalDateTime.now());
        notification.setUser(assignee);
        notification.setTask(task);
        Notification persistedNotification = notificationRepository.save(notification);
        String text = String.format("""
                        Hello %s,
                        
                        You have been assigned to the following task: %s.
                        
                        Kind regards,
                        Home Management Team
                        """,
                assignee.getFullName(), task.getTitle());
        emailSenderService.sendSimpleMessage(
                assignee,
                "You have been assigned to a new task",
                text);
        return persistedNotification;
    }

    public Notification getById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Notification notification = notificationRepository.findById(id).orElseThrow(() ->
                new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "notification", id));
        if (familyService.getFamilyMember(user, notification.getTask().getFamily()).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN); // TODO: Change message
        }
        return notification;
    }

    public List<Notification> getByTaskId(UUID taskId) {
        // ceva de genu trebuie aici, doar ca nu pot folosi taskService in notificationService ca imi da dependinta circulara
        // ce sa-i faaac? :(
//        User user = userService.getById(securityService.getUserId());
//        Task task = taskService.getById(taskId);
//        if (familyService.getFamilyMember(user, task.getFamily()).isEmpty()) {
//            throw new ForbiddenException(ErrorMessage.FORBIDDEN); // TODO: Change message
//        }
        return notificationRepository.findNotificationsByTaskId(taskId);
    }
}
