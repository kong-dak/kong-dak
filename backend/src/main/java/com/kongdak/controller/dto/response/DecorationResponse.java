package com.kongdak.controller.dto.response;

import com.kongdak.domain.diary.DecorationType;
import com.kongdak.domain.diary.DiaryDecoration;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "데코레이션 응답")
public record DecorationResponse(
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

        @Schema(description = "스타일 정보", example = "size: 2rem;")
        String style
) {
    public static DecorationResponse from(DiaryDecoration decoration) {
        return new DecorationResponse(
                decoration.getId(),
                decoration.getType(),
                decoration.getContent(),
                decoration.getPositionX(),
                decoration.getPositionY(),
                decoration.getStyle()
        );
    }
}
