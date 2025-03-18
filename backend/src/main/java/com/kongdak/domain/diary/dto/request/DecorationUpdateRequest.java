package com.kongdak.domain.diary.dto.request;

import com.kongdak.domain.diary.entity.DecorationType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "데코레이션 수정 요청")
public record DecorationUpdateRequest(
        @Schema(description = "데코레이션 ID", example = "1")
        Long decorationId,

        @Schema(description = "데코레이션 타입", example = "STICKER")
        DecorationType type,

        @Schema(description = "데코레이션 내용", example = "❤️")
        String content,

        @Schema(description = "X 좌표", example = "100")
        Integer positionX,

        @Schema(description = "Y 좌표", example = "100")
        Integer positionY,

        @Schema(description = "스타일 정보", example = "{\"fontSize\": \"2rem\", \"color\": \"red\"}")
        String style
) {}
