package com.kongdak.domain.dailyquestion.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이모지 요청")
public record EmojiRequest(
        @Schema(description = "이모지", example = "👍")
        String emoji
) {}
