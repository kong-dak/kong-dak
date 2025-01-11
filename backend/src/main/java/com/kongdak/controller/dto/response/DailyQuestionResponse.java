package com.kongdak.controller.dto.response;

import com.kongdak.domain.dailyquestion.DailyQuestion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "데일리 질문 응답")
public record DailyQuestionResponse(
        @Schema(description = "질문 ID", example = "1")
        Long questionId,
        @Schema(description = "질문 내용", example = "오늘 하루 중 가장 행복했던 순간은?")
        String title,

        @Schema(description = "답변 여부", example = "false")
        boolean isAnswered,
        @Schema(description = "생성 일자", example = "2024-01-10 12:00")
        LocalDateTime createdAt
) {
    public static DailyQuestionResponse from(DailyQuestion question) {
        return new DailyQuestionResponse(
                question.getId(),
                question.getTitle(),
                question.isAnswered(),
                question.getCreatedAt()
        );
    }
}