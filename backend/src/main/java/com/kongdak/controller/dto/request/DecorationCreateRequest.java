package com.kongdak.controller.dto.request;

import com.kongdak.domain.diary.DecorationType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "데코레이션 생성 요청")
public record DecorationCreateRequest(
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
