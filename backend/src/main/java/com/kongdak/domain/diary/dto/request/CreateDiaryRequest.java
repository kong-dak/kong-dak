package com.kongdak.domain.diary.dto.request;

import com.kongdak.domain.diary.entity.Emotion;
import com.kongdak.domain.diary.entity.Weather;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "다이어리 생성 요청")
public record CreateDiaryRequest(
        @Schema(description = "다이어리 내용", example = "오늘은 날씨가 좋았다...", required = true)
        @NotNull
        String content,

        @Schema(description = "감정 상태", example = "HAPPY", required = true)
        Emotion emotion,

        @Schema(description = "날씨", example = "SUNNY", required = true)
        Weather weather,

        @Schema(description = "다이어리 작성 날짜", example = "2024-01-10", required = true)
        @NotNull
        LocalDate diaryDate,

        @Schema(description = "첨부된 사진 URL 목록", example = "[\"photo-keys/uuid1.jpg\", \"photo-keys/uuid2.jpg\"]")
        List<String> photoUrls,

        @Schema(description = "데코레이션 목록")
        List<DecorationCreateRequest> decorations
) {}
