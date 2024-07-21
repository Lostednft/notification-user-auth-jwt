package com.Notification.services;

import com.Notification.domain.Notification;
import com.Notification.domain.Status;
import com.Notification.domain.User;
import com.Notification.dtos.notification.RequestNotification;
import com.Notification.repositories.NotificationRepository;
import com.Notification.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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


        var notificationSave = new Notification();

        notificationSave.setDate(requestNotification.dateTime());
        notificationSave.setMessage(requestNotification.message());
        notificationSave.setUserSender(userLocalSender.getLogin());
        notificationSave.setUserDestination(userLocalDestination.getLogin());
        notificationSave.setChannel(requestNotification.channel().toChannel());
        notificationSave.setStatus(requestNotification.status().toStatus());

        notificationRepository.save(notificationSave);
    }


    public Notification findNotificationById(Long idNotification) {
        var localNotification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new RuntimeException("There is no Notification with this ID."));
        return localNotification;
    }

    @Transactional
    public void deleteNotificationById(Long idNotification) {
        var localNotification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new RuntimeException(("Delete Failed! There is no Notification with this ID.")));
        notificationRepository.delete(localNotification);
    }

    @Transactional
    public void checkAndSend(LocalDateTime dateTime) {

        var notifications = notificationRepository.findByStatusIn(
                List.of(Status.StatusLoad.PENDING.toStatus(), Status.StatusLoad.ERROR.toStatus()));

            List<Notification> notificationsToUpdate = notifications.stream()
                    .filter(not -> !not.getDate().isAfter(dateTime))
                    .peek(not -> not.setStatus(Status.StatusLoad.SUCCESS.toStatus()))
                    .collect(Collectors.toList());

        if (!notificationsToUpdate.isEmpty()) notificationRepository.saveAll(notificationsToUpdate);
    }
}