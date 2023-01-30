package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.model.Notification;
import com.amss.homemanagement.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;


    //public Notification create(Notification notification) { }

    //public Notification updateById(UUID id, Notification notification) { }

    public Notification getById(UUID id) {
        return notificationRepository.findById(id).orElseThrow(() ->
                new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "notification", id));
    }

    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    public void deleteById(UUID id) {
        Notification notification = getById(id);
        notificationRepository.delete(notification);
    }

}
