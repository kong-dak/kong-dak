package com.kongdak.controller;

import com.kongdak.domain.notification.dto.response.NotificationListResponse;
import com.kongdak.domain.notification.dto.response.NotificationResponse;
import com.kongdak.domain.notification.entity.NotificationEvent;
import com.kongdak.domain.notification.service.NotificationService;
import com.kongdak.domain.notification.service.SseEmitterService;
import com.kongdak.global.response.BaseResponse;
import com.kongdak.global.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
@Tag(name = "알림", description = "알림 관련 API")
public class NotificationController {

    private final SseEmitterService sseEmitterService;
    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "알림 구독", description = "SSE 연결을 통해 실시간 알림을 구독합니다.")
    public SseEmitter subscribe(
            @Parameter(description = "인증된 사용자 ID", hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return sseEmitterService.connect(userDetails.getId());
    }

    @GetMapping
    @Operation(summary = "알림 목록 조회", description = "사용자의 알림 목록을 조회합니다.")
    public BaseResponse<NotificationListResponse> getNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<NotificationEvent> notifications = notificationService.getNotifications(userDetails.getId());

        NotificationListResponse response = new NotificationListResponse(notifications.stream()
                                                                                      .map(NotificationResponse::from)
                                                                                      .toList());

        return BaseResponse.ok(response);
    }

    @GetMapping("/unread")
    @Operation(summary = "읽지 않은 알림 조회", description = "사용자의 읽지 않은 알림을 조회합니다.")
    public BaseResponse<NotificationListResponse> getUnreadNotifications(
            @Parameter(description = "인증된 사용자 ID", hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<NotificationEvent> notifications = notificationService.getUnreadNotifications(userDetails.getId());

        NotificationListResponse response = new NotificationListResponse(notifications.stream()
                                                                                      .map(NotificationResponse::from)
                                                                                      .collect(Collectors.toList()));

        return BaseResponse.ok(response);
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 상태로 변경합니다.")
    public BaseResponse<Void> markAsRead(
            @Parameter(description = "알림 ID", required = true) @PathVariable Long notificationId,
            @Parameter(description = "인증된 사용자 ID", hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        notificationService.markAsRead(notificationId, userDetails.getId());
        return BaseResponse.ok();
    }

    @PostMapping("/poke")
    @Operation(summary = "콕 찌르기", description = "상대방에게 콕 찌르기 알림을 보냅니다.")
    public BaseResponse<Void> pokePartner(
            @Parameter(description = "인증된 사용자 ID", hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getId();
        notificationService.sendPokeNotification(memberId, null); // 상대방 ID는 서비스에서 조회
        return BaseResponse.ok();
    }


    @GetMapping("/status")
    @Operation(summary = "알림 연결 상태 조회", description = "현재 SSE 연결 상태를 조회합니다. (관리자용)")
    public BaseResponse<Map<String, Object>> getConnectionStatus() {
        return BaseResponse.ok(sseEmitterService.getConnectionStatus());
    }
}