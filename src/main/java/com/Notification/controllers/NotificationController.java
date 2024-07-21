package com.Notification.controllers;

import com.Notification.dtos.notification.RequestNotification;
import com.Notification.services.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class    NotificationController {

    private final NotificationService notificationService;
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity register(@RequestBody @Valid RequestNotification notification){
        notificationService.registerNotification(notification);
        return ResponseEntity.ok("Registered was success.");
    }

    @GetMapping("/{idNotification}")
    public ResponseEntity getNotification(@PathVariable @Valid Long idNotification){
        return ResponseEntity.ok(notificationService.findNotificationById(idNotification));
    }

    @PostMapping("/{deleteId}")
    public ResponseEntity deleteNotification(@PathVariable @Valid Long deleteId){
        notificationService.deleteNotificationById(deleteId);
        return ResponseEntity.ok("Notification deleted with success.");
    }
}
