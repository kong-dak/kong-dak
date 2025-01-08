package com.kongdak.controller.dto.request;

import com.kongdak.domain.diary.DecorationType;

public record DecorationCreateRequest(
        DecorationType type,
        String content,
        Integer positionX,
        Integer positionY,
        String style
) {}

