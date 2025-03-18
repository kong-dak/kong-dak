package com.kongdak.domain.dailyquestion.event;

import com.kongdak.global.event.DomainEvent;

public record DailyQuestionAnsweredEvent(
        Long questionId,
        Long answerId,
        Long answererId,
        Long partnerId,
        String questionText
) implements DomainEvent {

    @Override
    public String getEventType() {
        return "DAILY_QUESTION_ANSWERED";
    }
}
