package com.kongdak.controller.dto.response;

import com.kongdak.domain.dailyquestion.DailyQuestion;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "데일리 질문 목록 응답")
public record DailyQuestionListResponse(
        @Schema(description = "질문 ID", example = "1")
        Long questionId,

        @Schema(description = "질문 제목", example = "오늘 가장 행복했던 순간은?")
        String title
) {
    public static DailyQuestionListResponse from(DailyQuestion question) {
        return new DailyQuestionListResponse(
                question.getId(),
                question.getTitle()
        );
    }
}