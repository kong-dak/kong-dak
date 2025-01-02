package com.kongdak.controller.dto.response;

import com.kongdak.domain.diary.DecorationType;
import com.kongdak.domain.diary.DiaryDecoration;

public record DecorationResponse(
        Long decorationId,
        DecorationType type,
        String content,
        Integer positionX,
        Integer positionY,
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
