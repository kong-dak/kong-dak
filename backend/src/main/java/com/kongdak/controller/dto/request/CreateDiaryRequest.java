package com.kongdak.controller.dto.request;

import com.kongdak.domain.diary.Emotion;
import com.kongdak.domain.diary.Weather;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateDiaryRequest(
        @NotNull
        String content,

        @NotNull
        Emotion emotion,

        @NotNull
        Weather weather,

        @NotNull
        LocalDate diaryDate,

        List<String> photoUrls,
        List<DecorationCreateRequest> decorations
) {
}
