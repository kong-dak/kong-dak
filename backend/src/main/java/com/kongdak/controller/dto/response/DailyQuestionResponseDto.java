package com.kongdak.controller.dto.response;

import com.kongdak.domain.dailyquestion.DailyQuestion;

import java.time.LocalDateTime;

public record DailyQuestionResponseDto(
        Long id,
        String title,
        boolean isAnswered,
        LocalDateTime createdAt
) {
    public static DailyQuestionResponseDto from(DailyQuestion question) {
        return new DailyQuestionResponseDto(
                question.getId(),
                question.getTitle(),
                question.isAnswered(),
                question.getCreatedAt()
        );
    }
}