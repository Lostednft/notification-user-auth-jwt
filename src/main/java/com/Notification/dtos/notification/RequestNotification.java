package com.Notification.dtos.notification;

import com.Notification.domain.Channel;
import com.Notification.domain.Status;

public record RequestNotification(Long time,
                                  String message,
                                  String loginSender,
                                  String loginDestination,
                                  Status.StatusLoad status,
                                  Channel.ChannelLoad channel) {
}
