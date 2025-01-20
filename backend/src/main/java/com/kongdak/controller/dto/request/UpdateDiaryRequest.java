package com.kongdak.controller.dto.request;

import com.kongdak.domain.diary.Emotion;
import com.kongdak.domain.diary.Weather;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "다이어리 수정 요청")
public record UpdateDiaryRequest(
        @Schema(description = "다이어리 내용", example = "수정된 내용...", required = true)
        @NotNull
        String content,

        @Schema(description = "감정 상태", example = "HAPPY", required = true)
        @NotNull
        Emotion emotion,

        @Schema(description = "날씨", example = "SUNNY", required = true)
        @NotNull
        Weather weather,

        @Schema(description = "첨부된 사진 URL 목록", example = "[\"photo1.jpg\", \"photo2.jpg\"]")
        List<String> photoUrls,

        @Schema(description = "데코레이션 목록")
        List<DecorationUpdateRequest> decorations
) {}
