package com.kongdak.domain.diary.event;

import com.kongdak.global.event.DomainEvent;

public record DiaryEmojiEvent(
        Long diaryId,
        Long reactorId,
        Long authorId,
        String emojiType
) implements DomainEvent {

    @Override
    public String getEventType() {
        return "DIARY_EMOJI";
    }
}