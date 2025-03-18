package com.kongdak.domain.dailyquestion.event;

import com.kongdak.global.event.DomainEvent;

import java.time.LocalDate;

public record DailyQuestionNewEvent(
        Long questionId,
        Long receiverId,
        String questionText,
        LocalDate questionDate
) implements DomainEvent {

    @Override
    public String getEventType() {
        return "DAILY_QUESTION_NEW";
    }
}
