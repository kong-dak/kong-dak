package com.kongdak.controller.dto.response;

import com.kongdak.domain.dailyquestion.DailyAnswer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "데일리 답변 응답")
public record DailyAnswerResponse(
        @Schema(description = "답변 ID", example = "1")
        Long answerId,

        @Schema(description = "작성자 ID", example = "1")
        Long memberId,

        @Schema(description = "답변 내용", example = "오늘의 답변입니다.")
        String content,

        @Schema(description = "작성 시간", example = "2024-01-10T12:00:00")
        LocalDateTime createdAt
) {
    public static DailyAnswerResponse from(DailyAnswer answer) {
        return new DailyAnswerResponse(
                answer.getId(),
                answer.getMember().getId(),
                answer.getContent(),
                answer.getCreatedAt()
        );
    }
}
