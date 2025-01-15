package com.kongdak.domain.notification;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationMessage {
    private NotificationType type;
    private String message;
    private String requestId;
    private Long requesterId;
    private LocalDateTime timestamp;

    public NotificationMessage(NotificationType type, String requestId, Long requesterId) {
        this.type = type;
        this.message = type.getDefaultMessage();
        this.requestId = requestId;
        this.requesterId = requesterId;
        this.timestamp = LocalDateTime.now();
    }

    public NotificationMessage(NotificationType type) {
        this.type = type;
        this.message = type.getDefaultMessage();
        this.timestamp = LocalDateTime.now();
    }
}