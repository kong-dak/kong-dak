package com.kongdak.domain.dailyquestion.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "답변 수정 요청")
public record DailyAnswerUpdateRequest(
        @Schema(description = "답변 내용", example = "친구와 맛있는 점심을 먹었을 때")
        @NotBlank(message = "답변 내용은 필수입니다")
        String content
) {
}


