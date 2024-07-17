package com.Notification.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    private LocalDateTime date;

    private String userSender;

    private String userDestination;

    private String status;

    private String channel;

    public Notification(LocalDateTime date, String message, String userSender, String userDestination, String status, String channel) {
        this.date = date;
        this.message = message;
        this.userSender = userSender;
        this.userDestination = userDestination;
        this.status = status;
        this.channel = channel;
    }
}

