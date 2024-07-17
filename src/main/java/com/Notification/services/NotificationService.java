package com.Notification.services;

import com.Notification.domain.Notification;
import com.Notification.dtos.notification.RequestNotification;
import com.Notification.repositories.NotificationRepository;
import com.Notification.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void registerNotification(RequestNotification requestNotification){

        var userLocalSender = userRepository.findByLogin(requestNotification.loginSender());
        var userLocalDestination = userRepository.findByLogin(requestNotification.loginDestination());

        if(userLocalSender == null)
            throw new BadCredentialsException("Login Sender is invalid");

        if(userLocalDestination == null)
            throw new BadCredentialsException("Login Destination is invalid");

        var timerPlusSeconds = requestNotification.time();

        var notificationSave = new Notification();
        notificationSave.setDate(LocalDateTime.now().plusSeconds(timerPlusSeconds));
        notificationSave.setMessage(requestNotification.message());
        notificationSave.setUserSender(userLocalSender);
        notificationSave.setUserDestination(userLocalDestination);
        notificationSave.setChannel(requestNotification.channel().toChannel());
        notificationSave.setStatus(requestNotification.status().toStatus());

        notificationRepository.save(notificationSave);
    }
}
