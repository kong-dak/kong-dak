package com.kongdak.controller.dto.request;

import com.kongdak.domain.diary.Emotion;
import com.kongdak.domain.diary.Weather;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateDiaryRequest(
        @NotNull
        String content,

        @NotNull
        Emotion emotion,

        @NotNull
        Weather weather,

        List<String> photoUrls,
        List<DecorationUpdateRequest> decorations
) {}
