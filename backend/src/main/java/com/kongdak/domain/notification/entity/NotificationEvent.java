package com.kongdak.domain.notification.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "notification_event")
@Getter
@NoArgsConstructor
public class NotificationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 알림 고유 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type; // 알림 타입

    @Column(nullable = false)
    private String message; // 알림 메시지

    @Column
    private Long requesterId; // 요청자 ID

    @Column(nullable = false)
    private Long receiverId; // 수신자 ID

    @Column(name = "additional_data", columnDefinition = "TEXT")
    private String additionalDataJson; // 추가 데이터 (JSON 문자열)

    @Column(name = "is_read", nullable = false)
    private boolean isRead; // 읽음 상태

    @Column(nullable = false)
    private LocalDateTime timestamp; //생성 시간

    @Transient
    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Builder 패턴으로 객체 생성
    @Builder(builderMethodName = "builder")
    private NotificationEvent(
            Long id,
            NotificationType type,
            String message,
            Long requesterId,
            Long receiverId,
            String additionalDataJson,
            boolean isRead,
            LocalDateTime timestamp
    ) {
        this.id = id;
        this.type = type;
        this.message = message != null ? message : type.getDefaultMessage();
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.additionalDataJson = additionalDataJson;
        this.isRead = isRead;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }
    public static class NotificationEventBuilder {
        private Map<String, Object> dataMap = new HashMap<>();

        // 메시지가 null이면 타입의 기본 메시지 사용
        public NotificationEventBuilder message(String message) {
            this.message = message;
            return this;
        }

        // 현재 시간으로 타임스탬프 설정
        public NotificationEventBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
            return this;
        }

        // 읽지 않음으로 기본 설정
        public NotificationEventBuilder isRead(boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        // 추가 데이터 설정
        public NotificationEventBuilder addData(String key, Object value) {
            if (this.dataMap == null) {
                this.dataMap = new HashMap<>();
            }
            this.dataMap.put(key, value);
            return this;
        }


    }
    // 정적 생성 메서드들
    public static NotificationEventBuilder create(NotificationType type, Long receiverId) {
        return builder()
                .type(type)
                .receiverId(receiverId)
                .isRead(false)
                .timestamp(LocalDateTime.now());
    }

    public static NotificationEventBuilder create(NotificationType type, Long requesterId, Long receiverId) {
        return builder()
                .type(type)
                .requesterId(requesterId)
                .receiverId(receiverId)
                .isRead(false)
                .timestamp(LocalDateTime.now());
    }

    public static NotificationEventBuilder createWithRequest(NotificationType type, String requestId, Long requesterId, Long receiverId) {
        return builder()
                .type(type)
                .requesterId(requesterId)
                .receiverId(receiverId)
                .addData("requestId", requestId)  // requestId를 추가 데이터로 이동
                .isRead(false)
                .timestamp(LocalDateTime.now());
    }

    // 기존 팩토리 메서드들 (하위 호환성)
    public static NotificationEvent of(NotificationType type, Long receiverId) {
        return create(type, receiverId).build();
    }

    public static NotificationEvent of(NotificationType type, Long requesterId, Long receiverId) {
        return create(type, requesterId, receiverId).build();
    }

    public static NotificationEvent of(NotificationType type, String message, Long receiverId) {
        return create(type, receiverId)
                .message(message)
                .build();
    }

    public static NotificationEvent ofRequest(NotificationType type, String requestId, Long requesterId, Long receiverId) {
        return createWithRequest(type, requestId, requesterId, receiverId).build();
    }

    // 추가 데이터 관련 메서드
    public void addData(String key, Object value) {
        try {
            Map<String, Object> dataMap = getDataMap();
            dataMap.put(key, value);
            this.additionalDataJson = objectMapper.writeValueAsString(dataMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to add data to notification", e);
        }
    }

    public Object getData(String key) {
        Map<String, Object> dataMap = getDataMap();
        return dataMap.get(key);
    }

    public Map<String, Object> getAllData() {
        return getDataMap();
    }

    // requestId를 추가 데이터에서 가져오는 편의 메서드 (하위 호환성)
    public String getRequestId() {
        Object requestId = getData("requestId");
        return requestId != null ? requestId.toString() : null;
    }

    @JsonIgnore
    private Map<String, Object> getDataMap() {
        if (additionalDataJson == null || additionalDataJson.isEmpty()) {
            return new HashMap<>();
        }

        try {
            return objectMapper.readValue(additionalDataJson,
                                          new TypeReference<HashMap<String, Object>>() {});
        } catch (JsonProcessingException e) {
            return new HashMap<>();
        }
    }

    // 읽음 처리 메서드
    public NotificationEvent markAsRead() {
        this.isRead = true;
        return this;
    }
}