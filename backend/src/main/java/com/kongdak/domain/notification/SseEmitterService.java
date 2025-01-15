package com.kongdak.domain.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseEmitterService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 60 minutes
    private static final String EMITTER_PREFIX = "emitter:";

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public SseEmitter connect(Long memberId) {
        String emitterId = getEmitterId(memberId);
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        sendDummyEvent(emitter);

        emitter.onCompletion(() -> {
            log.info("SSE Connection completed: {}", emitterId);
            removeEmitter(emitterId);
        });
        emitter.onTimeout(() -> {
            log.info("SSE Connection timeout: {}", emitterId);
            emitter.complete();
            removeEmitter(emitterId);
        });
        emitter.onError(ex -> {
            log.error("SSE Connection error: {}", emitterId, ex);
            emitter.complete();
            removeEmitter(emitterId);
        });

        emitters.put(emitterId, emitter);
        log.info("SSE Connection created: {}", emitterId);

        return emitter;
    }

    public void sendToMember(Long memberId, NotificationMessage message) {
        String emitterId = getEmitterId(memberId);
        SseEmitter emitter = emitters.get(emitterId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(message.getRequestId())
                        .name(message.getType().name())
                        .data(message, MediaType.APPLICATION_JSON));
                log.info("Notification sent to {}: {}", memberId, message.getType());
            } catch (IOException e) {
                log.error("Failed to send notification to {}", memberId, e);
                removeEmitter(emitterId);
            }
        }
    }

    private void sendDummyEvent(SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event()
                    .id("0")
                    .name("connect")
                    .data("connected"));
        } catch (IOException e) {
            log.error("Failed to send dummy event", e);
        }
    }

    private String getEmitterId(Long memberId) {
        return EMITTER_PREFIX + memberId;
    }

    private void removeEmitter(String emitterId) {
        emitters.remove(emitterId);
    }

    public void closeAll() {
        emitters.forEach((key, emitter) -> {
            try {
                emitter.complete();
                log.info("SSE Connection closed: {}", key);
            } catch (Exception e) {
                log.error("Failed to close SSE connection: {}", key, e);
            }
        });
        emitters.clear();
    }
}