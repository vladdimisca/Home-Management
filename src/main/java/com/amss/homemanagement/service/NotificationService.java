package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.model.Notification;
import com.amss.homemanagement.model.Task;
import com.amss.homemanagement.model.User;
import com.amss.homemanagement.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        Notification notification = new Notification.NotificationBuilder()
                .date(LocalDateTime.now())
                .user(assignee)
                .task(task)
                .build();
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
            throw new ExceptionFactory().createException(HttpStatus.FORBIDDEN, ErrorMessage.NOT_PART_OF_FAMILY);
        }
        return notification;
    }

}
