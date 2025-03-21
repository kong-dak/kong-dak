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
    public void markAsRead(Long notificationId, Long memberId) {
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
    public void sendCoupleRequestNotification(String requestId, Long requesterId, Long receiverId) {
        NotificationEvent notification = NotificationEvent.createWithRequest(NotificationType.COUPLE_MATCH_REQUEST,
                                                                             requestId,
                                                                             requesterId,
                                                                             receiverId)
                                                          .build();
        sendNotification(notification);
    }

    /**
     * 커플 연결 수락 알림 전송
     */
    @Transactional
    public void sendCoupleAcceptedNotification(Long requesterId, Long receiverId) {
        NotificationEvent notification = NotificationEvent.create(NotificationType.COUPLE_MATCH_ACCEPTED,
                                                                  requesterId,
                                                                  receiverId
                                                          )
                                                          .build();

        sendNotification(notification);
    }

    /**
     * 커플 연결 거절 알림 전송
     */
    @Transactional
    public void sendCoupleRejectedNotification(Long requesterId, Long receiverId) {
        NotificationEvent notification = NotificationEvent.create(NotificationType.COUPLE_MATCH_REJECTED,
                                                                  requesterId,
                                                                  receiverId
                                                          )
                                                          .build();
        sendNotification(notification);
    }

    /**
     * 콕 찌르기 알림 전송
     */
    @Transactional
    public void sendPokeNotification(Long senderId, Long receiverId) {
        NotificationEvent notification = NotificationEvent.create(NotificationType.POKE, senderId, receiverId)
                                                          .build();
        sendNotification(notification);
    }

    /**
     * 기념일 알림 전송
     */
    @Transactional
    public void sendAnniversaryNotification(Long memberId1, Long memberId2) {


        NotificationEvent notification1 = NotificationEvent.create(NotificationType.COUPLE_ANNIVERSARY, memberId1)
                                                           .build();
        NotificationEvent notification2 = NotificationEvent.create(NotificationType.COUPLE_ANNIVERSARY, memberId2)
                                                           .build();

        sendNotification(notification1);
        sendNotification(notification2);
    }


    /**
     * 일정 관련 알림 전송 (생성, 수정)
     */
    private NotificationEvent createScheduleNotification(NotificationType type,
                                                         Long scheduleId,
                                                         Long actorId,
                                                         Long partnerId) {
        return NotificationEvent.create(type, actorId, partnerId)
                                .addData("scheduleId", scheduleId.toString())
                                .build();
    }

    /**
     * 일정 생성 알림 전송
     */
    @Transactional
    public void sendScheduleCreatedNotification(Long scheduleId, Long creatorId, Long partnerId) {
        NotificationEvent notification = createScheduleNotification(NotificationType.SCHEDULE_CREATED,
                                                                    scheduleId,
                                                                    creatorId,
                                                                    partnerId);
        sendNotification(notification);
    }

    /**
     * 일정 수정 알림 전송
     */
    @Transactional
    public void sendScheduleUpdatedNotification(Long scheduleId, Long updaterId, Long partnerId) {
        NotificationEvent notification = createScheduleNotification(NotificationType.SCHEDULE_UPDATED,
                                                                    scheduleId,
                                                                    updaterId,
                                                                    partnerId);
        sendNotification(notification);
    }

    /**
     * 일정 리마인더 알림 전송 (스케줄러에서 호출)
     */
    @Transactional
    public void sendScheduleReminderNotification(Long scheduleId, List<Long> memberIds) {
        for (Long memberId : memberIds) {
            NotificationEvent notification = NotificationEvent.create(NotificationType.SCHEDULE_REMINDER, memberId)
                                                              .addData("scheduleId", scheduleId.toString())
                                                              .build();
            sendNotification(notification);
        }
    }


    /**
     * 일기 작성 알림 전송
     */
    @Transactional
    public void sendDiaryCreatedNotification(Long diaryId, Long authorId, Long partnerId) {
        NotificationEvent notification = NotificationEvent.create(NotificationType.DIARY_CREATED, authorId, partnerId)
                                                          .addData("diaryId", diaryId.toString())
                                                          .build();
        sendNotification(notification);
    }

    /**
     * 오늘의 질문 이모지 반응 알림 전송
     */
    @Transactional
    public void sendDailyQuestionEmojiNotification(Long answerId, Long reactorId, Long authorId) {
        NotificationEvent notification = NotificationEvent.create(NotificationType.DAILY_QUESTION_EMOJI_CREATED, reactorId, authorId)
                                                          .addData("answerId", answerId.toString())
                                                          .build();
        sendNotification(notification);
    }

    /**
     * 오늘의 질문 알림 전송
     */
    @Transactional
    public void sendDailyQuestionNotification(Long questionId, List<Long> memberIds) {
        for (Long memberId : memberIds) {
            NotificationEvent notification = NotificationEvent.create(NotificationType.DAILY_QUESTION_NEW, memberId)
                                                              .addData("questionId", questionId.toString())
                                                              .build();
            sendNotification(notification);
        }
    }

    /**
     * 질문 답변 알림 전송
     */
    @Transactional
    public void sendQuestionAnsweredNotification(Long questionId, Long answererId, Long partnerId) {
        NotificationEvent notification = NotificationEvent.create(NotificationType.DAILY_QUESTION_ANSWERED,
                                                                  answererId,
                                                                  partnerId)
                                                          .addData("questionId", questionId.toString())
                                                          .build();
        sendNotification(notification);
    }

    /**
     * 답변 댓글 알림 전송
     */
    @Transactional
    public void sendAnswerRepliedNotification(Long questionId, Long replierId, Long answererId) {
        NotificationEvent notification = NotificationEvent.create(NotificationType.DAILY_QUESTION_REPLIED,
                                                                  replierId,
                                                                  answererId)
                                                          .addData("questionId", questionId.toString())
                                                          .build();
        sendNotification(notification);
    }

    /**
     * 버킷리스트 생성 알림 전송
     */
    @Transactional
    public void sendBucketCreatedNotification(Long bucketId, Long creatorId, Long partnerId) {
        NotificationEvent notification = NotificationEvent.create(NotificationType.BUCKET_CREATED, creatorId, partnerId)
                                                          .addData("bucketId", bucketId.toString())
                                                          .build();
        sendNotification(notification);
    }

    /**
     * 버킷리스트 완료 알림 전송
     */
    @Transactional
    public void sendBucketCompletedNotification(Long bucketId, Long completerId, Long partnerId) {
        NotificationEvent notification = NotificationEvent.create(NotificationType.BUCKET_COMPLETED,
                                                                  completerId,
                                                                  partnerId)
                                                          .addData("bucketId", bucketId.toString())
                                                          .build();
        sendNotification(notification);
    }
}