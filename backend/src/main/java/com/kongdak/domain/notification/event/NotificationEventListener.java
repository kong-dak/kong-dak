package com.kongdak.domain.notification.event;

import com.kongdak.domain.bucketlist.event.BucketCompletedEvent;
import com.kongdak.domain.bucketlist.event.BucketCreatedEvent;
import com.kongdak.domain.calendar.event.ScheduleCreatedEvent;
import com.kongdak.domain.calendar.event.ScheduleReminderEvent;
import com.kongdak.domain.calendar.event.ScheduleUpdatedEvent;
import com.kongdak.domain.couple.event.CoupleAnniversaryEvent;
import com.kongdak.domain.couple.event.CoupleMatchAcceptedEvent;
import com.kongdak.domain.couple.event.CoupleMatchRejectedEvent;
import com.kongdak.domain.couple.event.CoupleMatchRequestedEvent;
import com.kongdak.domain.dailyquestion.event.DailyQuestionAnsweredEvent;
import com.kongdak.domain.dailyquestion.event.DailyQuestionNewEvent;
import com.kongdak.domain.dailyquestion.event.DailyQuestionRepliedEvent;
import com.kongdak.domain.diary.event.DiaryCreatedEvent;
import com.kongdak.domain.diary.event.DiaryEmojiEvent;
import com.kongdak.domain.notification.entity.NotificationEvent;
import com.kongdak.domain.notification.entity.NotificationType;
import com.kongdak.domain.notification.repository.NotificationRepository;
import com.kongdak.domain.notification.service.SseEmitterService;
import com.kongdak.global.event.DomainEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;
    private final SseEmitterService sseEmitterService;
    private Map<Class<?>, Function<Object, NotificationEvent>> eventHandlers;

    // TODO: 둘 모두에게 알림 가는 것 구현해야 함
    @PostConstruct
    public void initEventHandlers() {
        eventHandlers = Map.ofEntries(
                // 커플 관련 이벤트
                Map.entry(CoupleMatchRequestedEvent.class, event -> {
                    CoupleMatchRequestedEvent e = (CoupleMatchRequestedEvent) event;
                    return NotificationEvent.ofRequest(
                            NotificationType.COUPLE_MATCH_REQUEST,
                            e.requestId(),
                            e.requesterId(),
                            e.receiverId()
                    );
                }),
                Map.entry(CoupleMatchAcceptedEvent.class, event -> {
                    CoupleMatchAcceptedEvent e = (CoupleMatchAcceptedEvent) event;
                    return NotificationEvent.of(
                            NotificationType.COUPLE_MATCH_ACCEPTED,
                            e.receiverId(),
                            e.requesterId()
                    );
                }),
                Map.entry(CoupleMatchRejectedEvent.class, event -> {
                    CoupleMatchRejectedEvent e = (CoupleMatchRejectedEvent) event;
                    return NotificationEvent.of(
                            NotificationType.COUPLE_MATCH_REJECTED,
                            e.receiverId(),
                            e.requesterId()
                    );
                }),
                Map.entry(CoupleAnniversaryEvent.class, event -> {
                    CoupleAnniversaryEvent e = (CoupleAnniversaryEvent) event;
                    // 두 사용자 모두에게 알림 보내기 위한 처리가 필요할 수 있음
                    // 여기서는 user1에게만 보내는 것으로 가정
                    return NotificationEvent.of(
                            NotificationType.COUPLE_ANNIVERSARY, (Long) null, // 시스템 발신
                            e.user1Id()
                    );
                }),

                // 일정 관련 이벤트
                Map.entry(ScheduleCreatedEvent.class, event -> {
                    ScheduleCreatedEvent e = (ScheduleCreatedEvent) event;
                    return NotificationEvent.of(
                            NotificationType.SCHEDULE_CREATED,
                            e.creatorId(),
                            e.partnerId()
                    );
                }),
                Map.entry(ScheduleUpdatedEvent.class, event -> {
                    ScheduleUpdatedEvent e = (ScheduleUpdatedEvent) event;
                    return NotificationEvent.of(
                            NotificationType.SCHEDULE_UPDATED,
                            e.updaterId(),
                            e.partnerId()
                    );
                }),
                Map.entry(ScheduleReminderEvent.class, event -> {
                    ScheduleReminderEvent e = (ScheduleReminderEvent) event;
                    return NotificationEvent.of(
                            NotificationType.SCHEDULE_REMINDER, (Long) null, // 시스템 발신
                            e.receiverId()
                    );
                }),

                // 일기 관련 이벤트
                Map.entry(DiaryCreatedEvent.class, event -> {
                    DiaryCreatedEvent e = (DiaryCreatedEvent) event;
                    return NotificationEvent.of(
                            NotificationType.DIARY_CREATED,
                            e.authorId(),
                            e.partnerId()
                    );
                }),
                Map.entry(DiaryEmojiEvent.class, event -> {
                    DiaryEmojiEvent e = (DiaryEmojiEvent) event;
                    return NotificationEvent.of(
                            NotificationType.DIARY_EMOJI,
                            e.reactorId(),
                            e.authorId()
                    );
                }),

                // 데일리 질문 관련 이벤트
                Map.entry(DailyQuestionNewEvent.class, event -> {
                    DailyQuestionNewEvent e = (DailyQuestionNewEvent) event;
                    return NotificationEvent.of(
                            NotificationType.DAILY_QUESTION_NEW, (Long) null, // 시스템 발신
                            e.receiverId()
                    );
                }),
                Map.entry(DailyQuestionAnsweredEvent.class, event -> {
                    DailyQuestionAnsweredEvent e = (DailyQuestionAnsweredEvent) event;
                    return NotificationEvent.of(
                            NotificationType.DAILY_QUESTION_ANSWERED,
                            e.answererId(),
                            e.partnerId()
                    );
                }),
                Map.entry(DailyQuestionRepliedEvent.class, event -> {
                    DailyQuestionRepliedEvent e = (DailyQuestionRepliedEvent) event;
                    return NotificationEvent.of(
                            NotificationType.DAILY_QUESTION_REPLIED,
                            e.replierId(),
                            e.answererId()
                    );
                }),

                // 버킷리스트 관련 이벤트
                Map.entry(BucketCreatedEvent.class, event -> {
                    BucketCreatedEvent e = (BucketCreatedEvent) event;
                    return NotificationEvent.of(
                            NotificationType.BUCKET_CREATED,
                            e.creatorId(),
                            e.partnerId()
                    );
                }),
                Map.entry(BucketCompletedEvent.class, event -> {
                    BucketCompletedEvent e = (BucketCompletedEvent) event;
                    return NotificationEvent.of(
                            NotificationType.BUCKET_COMPLETED,
                            e.completerId(),
                            e.partnerId()
                    );
                })
        );
    }

    @EventListener
    @Transactional
    public void handleDomainEvent(DomainEvent event) {
        Function<Object, NotificationEvent> handler = eventHandlers.get(event.getClass());
        if (handler != null) {
            NotificationEvent notification = handler.apply(event);
            saveAndSendNotification(notification);
        }
    }

    private void saveAndSendNotification(NotificationEvent notification) {
        NotificationEvent savedEvent = notificationRepository.save(notification);
        sseEmitterService.sendToMember(notification.getReceiverId(), savedEvent);
    }
}
