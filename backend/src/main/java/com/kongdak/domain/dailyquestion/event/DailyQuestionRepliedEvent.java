package com.kongdak.domain.dailyquestion.event;

import com.kongdak.global.event.DomainEvent;

public record DailyQuestionRepliedEvent(
        Long answerId,
        Long replyId,
        Long replierId,
        Long answererId,
        String replyContent
) implements DomainEvent {

    @Override
    public String getEventType() {
        return "DAILY_QUESTION_REPLIED";
    }
}
