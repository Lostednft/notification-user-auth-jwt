package com.Notification.controllers;

import com.Notification.dtos.notification.RequestNotification;
import com.Notification.services.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationControlle {

    private final NotificationService notificationService;

    public NotificationControlle(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity register(@RequestBody @Valid RequestNotification notification){

        notificationService.registerNotification(notification);

        return ResponseEntity.ok("Registered was success.");
    }
}
