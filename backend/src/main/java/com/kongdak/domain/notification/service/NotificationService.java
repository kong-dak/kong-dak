package com.kongdak.domain.notification.service;

import com.kongdak.domain.notification.entity.NotificationEvent;
import com.kongdak.domain.notification.entity.NotificationType;
import com.kongdak.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SseEmitterService sseEmitterService;

    /**
     * 알림 전송 및 저장
     */
    @Transactional
    public void sendNotification(NotificationEvent event) {
        log.debug("알림 전송 - 타입: {}, 수신자: {}", event.getType(), event.getReceiverId());

        // 알림 저장
        NotificationEvent savedEvent = notificationRepository.save(event);

        // SSE를 통해 실시간 알림 전송
        sseEmitterService.sendToMember(event.getReceiverId(), savedEvent);
    }

    /**
     * 사용자의 모든 알림 조회
     */
    @Transactional(readOnly = true)
    public List<NotificationEvent> getNotifications(Long memberId) {
        return notificationRepository.findByReceiverIdOrderByTimestampDesc(memberId);
    }

    /**
     * 사용자의 읽지 않은 알림 조회
     */
    @Transactional(readOnly = true)
    public List<NotificationEvent> getUnreadNotifications(Long memberId) {
        return notificationRepository.findByReceiverIdAndIsReadFalseOrderByTimestampDesc(memberId);
    }

    /**
     * 알림 읽음 처리
     */
    @Transactional
    public void markAsRead(
            Long notificationId,
            Long memberId
    ) {
        notificationRepository.findByIdAndReceiverId(notificationId, memberId)
                              .ifPresent(notification -> {
                                  notification.markAsRead();
                                  notificationRepository.save(notification);
                              });
    }

    /**
     * 커플 연결 요청 알림 전송
     */
    @Transactional
    public void sendCoupleRequestNotification(
            String requestId,
            Long requesterId,
            Long receiverId
    ) {
        NotificationEvent notification = NotificationEvent.ofRequest(NotificationType.COUPLE_MATCH_REQUEST, requestId, requesterId, receiverId);
        sendNotification(notification);
    }

    /**
     * 커플 연결 수락 알림 전송
     */
    @Transactional
    public void sendCoupleAcceptedNotification(
            Long requesterId,
            Long receiverId
    ) {
        NotificationEvent notification = NotificationEvent.of(NotificationType.COUPLE_MATCH_ACCEPTED, receiverId, requesterId);
        sendNotification(notification);
    }

    /**
     * 커플 연결 거절 알림 전송
     */
    @Transactional
    public void sendCoupleRejectedNotification(
            Long requesterId,
            Long receiverId
    ) {
        NotificationEvent notification = NotificationEvent.of(NotificationType.COUPLE_MATCH_REJECTED, receiverId, requesterId);
        sendNotification(notification);
    }

    /**
     * 콕 찌르기 알림 전송
     */
    @Transactional
    public void sendPokeNotification(
            Long senderId,
            Long receiverId
    ) {
        NotificationEvent notification = NotificationEvent.of(NotificationType.POKE, senderId, receiverId);
        sendNotification(notification);
    }
}