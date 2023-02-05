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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailSenderService emailSenderService;

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
        return notificationRepository.findById(id).orElseThrow(() ->
                new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "notification", id));
    }

    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

}
