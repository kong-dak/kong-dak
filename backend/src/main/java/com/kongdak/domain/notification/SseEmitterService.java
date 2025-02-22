package com.kongdak.domain.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
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
            removeEmitter(emitterId);
        });
        emitter.onTimeout(() -> {
            emitter.complete();
            removeEmitter(emitterId);
        });
        emitter.onError(ex -> {
            emitter.complete();
            removeEmitter(emitterId);
        });

        emitters.put(emitterId, emitter);

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
            } catch (IOException e) {
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
            throw new BusinessException(ErrorCode.CANNOT_SEND_DUMMY_EVENT);

        }
    }

    private String getEmitterId(Long memberId) {
        return EMITTER_PREFIX + memberId;
    }

    protected void removeEmitter(String emitterId) {
        emitters.remove(emitterId);
    }

    public void closeAll() {
        emitters.forEach((key, emitter) -> {
            try {
                emitter.complete();
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.CANNOT_CLOSE_EMITTER);
            }
        });
        emitters.clear();
    }
}