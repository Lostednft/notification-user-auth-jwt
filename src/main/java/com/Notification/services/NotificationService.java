package com.Notification.services;

import com.Notification.domain.Notification;
import com.Notification.domain.User;
import com.Notification.dtos.notification.RequestNotification;
import com.Notification.repositories.NotificationRepository;
import com.Notification.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void registerNotification(RequestNotification requestNotification){
        User userLocalSender = userRepository.findByLogin(requestNotification.loginSender());
        User userLocalDestination = userRepository.findByLogin(requestNotification.loginDestination());

        if(userLocalSender == null)
            throw new BadCredentialsException("Login Sender is invalid");

        if(userLocalDestination == null)
            throw new BadCredentialsException("Login Destination is invalid");

        var timerPlusSeconds = requestNotification.time();

        var notificationSave = new Notification();

        notificationSave.setDate(LocalDateTime.now().plusSeconds(timerPlusSeconds));
        notificationSave.setMessage(requestNotification.message());
        notificationSave.setUserSender(userLocalSender.getLogin());
        notificationSave.setUserDestination(userLocalDestination.getLogin());
        notificationSave.setChannel(requestNotification.channel().toChannel().getChannel());
        notificationSave.setStatus(requestNotification.status().toStatus().getStatus());

        notificationRepository.save(notificationSave);
    }


    public Notification findNotificationById(Long idNotification) {
        Notification localNotification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new RuntimeException("There is no Notification with this ID."));
        return localNotification;
    }

    @Transactional
    public void deleteNotificationById(Long idNotification) {
        var localNotification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new RuntimeException(("Delete Failed! There is no Notification with this ID.")));
        notificationRepository.delete(localNotification);
    }
}
