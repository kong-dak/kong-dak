package com.kongdak.domain.diary;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감정 상태")
public enum Emotion {
    @Schema(description = "행복", example = "HAPPY")
    HAPPY,

    @Schema(description = "슬픔", example = "SAD")
    SAD,

    @Schema(description = "화남", example = "ANGRY")
    ANGRY,

    @Schema(description = "신남", example = "EXCITED")
    EXCITED,

    @Schema(description = "긴장", example = "NERVOUS")
    NERVOUS
}