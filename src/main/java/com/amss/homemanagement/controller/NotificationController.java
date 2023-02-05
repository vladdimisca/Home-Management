package com.amss.homemanagement.controller;

import com.amss.homemanagement.dto.NotificationDto;
import com.amss.homemanagement.mapper.NotificationMapper;
import com.amss.homemanagement.model.Notification;
import com.amss.homemanagement.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationDto> getById(@PathVariable(name = "notificationId") UUID notificationId) {
        Notification notification = notificationService.getById(notificationId);
        return ResponseEntity.ok(notificationMapper.mapToDto(notification));
    }
}
