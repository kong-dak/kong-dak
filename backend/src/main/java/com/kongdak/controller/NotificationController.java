package com.kongdak.controller;

import com.kongdak.domain.notification.SseEmitterService;
import com.kongdak.global.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Tag(name = "알림", description = "알림 관련 API")
public class NotificationController {

    private final SseEmitterService sseEmitterService;

    @GetMapping(value = "/notifications/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "알림 구독", description = "SSE 연결을 통해 실시간 알림을 구독합니다.")
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return sseEmitterService.connect(Long.parseLong(userDetails.getUsername()));
    }
}
