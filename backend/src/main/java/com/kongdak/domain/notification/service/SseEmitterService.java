package com.kongdak.domain.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kongdak.domain.notification.entity.NotificationEvent;
import com.kongdak.domain.notification.repository.NotificationRepository;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseEmitterService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 60 minutes
    private static final String EMITTER_PREFIX = "emitter:";
    private static final String HEARTBEAT_EVENT = "heartbeat";

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;


    /**
     * 사용자 연결 생성
     */
    public SseEmitter connect(Long memberId) {
        String emitterId = getEmitterId(memberId);
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        log.debug("SSE 연결 생성: {}", emitterId);

        // 연결 직후 더미 이벤트 전송 (연결 확인용)
        sendDummyEvent(emitter);

        // 연결 종료 콜백 등록
        emitter.onCompletion(() -> {
            log.debug("SSE 연결 완료: {}", emitterId);
            removeEmitter(emitterId);
        });

        // 타임아웃 콜백 등록
        emitter.onTimeout(() -> {
            log.debug("SSE 연결 타임아웃: {}", emitterId);
            emitter.complete();
            removeEmitter(emitterId);
        });

        // 에러 콜백 등록
        emitter.onError(ex -> {
            log.debug("SSE 연결 에러: {}, 에러: {}", emitterId, ex.getMessage());
            emitter.complete();
            removeEmitter(emitterId);
        });

        // 기존 연결이 있으면 제거
        SseEmitter oldEmitter = emitters.get(emitterId);
        if (oldEmitter != null) {
            oldEmitter.complete();
        }

        // 새 연결 저장
        emitters.put(emitterId, emitter);

        return emitter;
    }

    /**
     * 특정 사용자에게 알림 전송
     */
    public void sendToMember(Long memberId, NotificationEvent message) {
        String emitterId = getEmitterId(memberId);
        SseEmitter emitter = emitters.get(emitterId);

        // 연결된 사용자일 때 실시간 전송
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(UUID.randomUUID().toString())
                        .name(message.getType().name())
                        .data(message, MediaType.APPLICATION_JSON));
                log.debug("알림 전송 성공: {}, 타입: {}", emitterId, message.getType());
            } catch (IOException e) {
                log.error("알림 전송 실패: {}, 에러: {}", emitterId, e.getMessage());
                emitter.complete();
                emitters.remove(emitterId);
            }
        } else {
            log.debug("사용자가 연결되어 있지 않음: {}", memberId);
        }
    }

    private void sendDummyEvent(SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event()
                    .id(UUID.randomUUID().toString())
                    .name("connect")
                    .data("connected"));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.CANNOT_SEND_DUMMY_EVENT);

        }
    }

    /**
     * 읽지 않은 알림 전송
     */
    private void sendUnreadNotifications(Long memberId, SseEmitter emitter) {
        try {
            List<NotificationEvent> unreadNotifications = notificationRepository
                    .findByReceiverIdAndIsReadFalseOrderByTimestampDesc(memberId);

            for (NotificationEvent notification : unreadNotifications) {
                try {
                    emitter.send(SseEmitter.event()
                            .id(notification.getId().toString())
                            .name(notification.getType().name())
                            .data(notification, MediaType.APPLICATION_JSON));
                } catch (IOException e) {
                    log.error("읽지 않은 알림 전송 실패: {}, 알림 ID: {}", memberId, notification.getId());
                }
            }
            log.debug("읽지 않은 알림 {} 개 전송 완료: {}", unreadNotifications.size(), memberId);
        } catch (Exception e) {
            log.error("읽지 않은 알림 조회/전송 실패: {}, 에러: {}", memberId, e.getMessage());
        }
    }

    /**
     * 주기적인 하트비트 전송 (연결 유지용)
     */
    @Scheduled(fixedRate = 30000) // 30초마다 실행
    public void sendHeartbeat() {
        emitters.forEach((id, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(HEARTBEAT_EVENT)
                        .data("ping"));
                log.debug("하트비트 전송: {}", id);
            } catch (IOException e) {
                log.debug("하트비트 전송 실패, 연결 제거: {}", id);
                emitter.complete();
                emitters.remove(id);
            }
        });
    }

    private String getEmitterId(Long memberId) {
        return EMITTER_PREFIX + memberId;
    }

    protected void removeEmitter(String emitterId) {
        emitters.remove(emitterId);
    }

    /**
     * 모든 연결 종료 (서버 종료 시 등에 사용)
     */
    public void closeAll() {
        emitters.forEach((key, emitter) -> {
            try {
                emitter.complete();
                log.debug("SSE 연결 종료: {}", key);
            } catch (Exception e) {
                log.error("SSE 연결 종료 실패: {}, 에러: {}", key, e.getMessage());
            }
        });
        emitters.clear();
        log.info("모든 SSE 연결 종료됨, 카운트: {}", emitters.size());
    }

    /**
     * 현재 연결 상태 조회 (모니터링용)
     */
    public Map<String, Object> getConnectionStatus() {
        Map<String, Object> status = new ConcurrentHashMap<>();
        status.put("activeConnections", emitters.size());
        status.put("connectionIds", emitters.keySet());
        return status;
    }
}