package com.Notification.services;

import com.Notification.domain.Notification;
import com.Notification.domain.Status;
import com.Notification.domain.User;
import com.Notification.dtos.notification.NotificationResponse;
import com.Notification.dtos.notification.NotificationUserResponse;
import com.Notification.dtos.notification.RequestNotification;
import com.Notification.repositories.NotificationRepository;
import com.Notification.repositories.UserRepository;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

        if (!notificationNullException(requestNotification))
            throw new NullPointerException("All fields must be filled in.");

        if(userLocalSender.getLogin().isEmpty())
            throw new BadCredentialsException("Login Sender is invalid");
        if(userLocalDestination.getLogin().isEmpty())
            throw new BadCredentialsException("Login Destination is invalid");

        notificationRepository.save(new Notification(requestNotification, userLocalSender, userLocalDestination));
    }

    @Transactional
    public NotificationResponse findNotificationById(Long idNotification) {
        var localNotification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new RuntimeException("There is no Notification with this ID."));

        var userSender = new NotificationUserResponse(
                localNotification.getUserSender().getId(),
                localNotification.getUserSender().getLogin());

        var userDestination = new NotificationUserResponse(
                localNotification.getUserDestination().getId(),
                localNotification.getUserDestination().getLogin());

        return new NotificationResponse(
                localNotification.getId(),
                localNotification.getMessage(),
                localNotification.getDate(),
                userSender,
                userDestination,
                localNotification.getStatus(),
                localNotification.getChannel());
    }

    @Transactional
    public void deleteNotificationById(Long idNotification) {
        var localNotification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new RuntimeException("Delete Failed! There is no Notification with this ID."));
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

        if (!notificationsToUpdate.isEmpty())
            notificationRepository.saveAll(notificationsToUpdate);
    }

    private boolean notificationNullException(RequestNotification request){

        return request.dateTime() != null &&
                !request.message().isEmpty() &&
                !request.loginSender().isEmpty() &&
                !request.loginDestination().isEmpty();
    }
}