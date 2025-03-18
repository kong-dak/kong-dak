package com.kongdak.domain.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_event")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 알림 고유 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type; // 알림 타입

    @Column(nullable = false)
    private String message; // 알림 메시지

    @Column(nullable = true)
    private String requestId; // 커플 요청 ID

    @Column
    private Long requesterId; // 요청자 ID

    @Column(nullable = false)
    private Long receiverId; // 수신자 ID

    @Transient //DB에 저장하지 않을 추가 데이터
    private Object data; //추가 데이터 (필요시)

    @Column(name = "is_read", nullable = false)
    private boolean isRead; // 읽음 상태

    @Column(nullable = false)
    private LocalDateTime timestamp; //생성 시간

    public static NotificationEvent of(NotificationType type, Long receiverId) {
        return NotificationEvent.builder()
                .type(type)
                .message(type.getDefaultMessage())
                .receiverId(receiverId)
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static NotificationEvent of(NotificationType type, Long requesterId, Long receiverId) {
        return NotificationEvent.builder()
                .type(type)
                .message(type.getDefaultMessage())
                .requesterId(requesterId)
                .receiverId(receiverId)
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static NotificationEvent of(NotificationType type, String message, Long receiverId) {
        return NotificationEvent.builder()
                .type(type)
                .message(message)
                .receiverId(receiverId)
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 커플 요청 관련 알림용 (requestId 포함)
    public static NotificationEvent ofRequest(NotificationType type, String requestId, Long requesterId, Long receiverId) {
        return NotificationEvent.builder()
                .type(type)
                .message(type.getDefaultMessage())
                .requestId(requestId)
                .requesterId(requesterId)
                .receiverId(receiverId)
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 읽음 처리 메서드
    public NotificationEvent markAsRead() {
        this.isRead = true;
        return this;
    }
}