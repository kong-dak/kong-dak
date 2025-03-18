package com.kongdak.domain.notification.dto.response;

import com.kongdak.domain.notification.entity.NotificationEvent;
import com.kongdak.domain.notification.entity.NotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
        Long id,
        NotificationType type,
        String message,
        Long requesterId,
        String requestId,
        boolean isRead,
        LocalDateTime timestamp
) {
    public static NotificationResponse from(NotificationEvent notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .message(notification.getMessage())
                .requesterId(notification.getRequesterId())
                .isRead(notification.isRead())
                .timestamp(notification.getTimestamp())
                .build();
    }
}
