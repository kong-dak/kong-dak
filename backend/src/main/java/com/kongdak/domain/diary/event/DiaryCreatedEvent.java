package com.kongdak.domain.diary.event;

import com.kongdak.global.event.DomainEvent;

import java.time.LocalDateTime;

public record DiaryCreatedEvent(
        Long diaryId,
        Long authorId,     // 일기 작성자 ID
        Long partnerId,    // 파트너 ID
        String title,
        LocalDateTime createdAt
) implements DomainEvent {

    @Override
    public String getEventType() {
        return "DIARY_CREATED";
    }
}