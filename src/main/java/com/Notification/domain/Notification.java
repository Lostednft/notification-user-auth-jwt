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

    @ManyToOne(fetch = FetchType.EAGER)
    private User userSender;

    @ManyToOne(fetch = FetchType.EAGER)
    private User userDestination;

    @ManyToOne(fetch = FetchType.EAGER)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    private Channel channel;

    public Notification(LocalDateTime date, String message, User userSender, User userDestination, Status status, Channel channel) {
        this.date = date;
        this.message = message;
        this.userSender = userSender;
        this.userDestination = userDestination;
        this.status = status;
        this.channel = channel;
    }
}

