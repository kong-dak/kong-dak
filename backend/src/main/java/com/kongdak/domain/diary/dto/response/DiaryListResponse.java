package com.kongdak.domain.diary.dto.response;

import com.kongdak.domain.diary.entity.Diary;
import com.kongdak.domain.diary.entity.Emotion;
import com.kongdak.domain.diary.entity.Weather;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "다이어리 목록 응답")
public record DiaryListResponse(
        @Schema(description = "다이어리 ID", example = "1")
        Long diaryId,

        @Schema(description = "다이어리 내용", example = "오늘은 정말 좋은 하루였다...")
        String content,

        @Schema(description = "감정 상태", example = "HAPPY")
        Emotion emotion,

        @Schema(description = "날씨", example = "SUNNY")
        Weather weather,

        @Schema(description = "다이어리 작성 날짜", example = "2024-01-10")
        LocalDate diaryDate,

        @Schema(description = "썸네일 이미지 URL", example = "https://example.com/thumbnail.jpg", nullable = true)
        String thumbnailUrl,

        @Schema(description = "작성 시간", example = "2024-01-10T12:00:00")
        LocalDateTime createdAt
) {
    public static DiaryListResponse from(Diary diary) {
        String thumbnail = diary.getPhotos().isEmpty() ?
                null :
                diary.getPhotos().get(0).getThumbnailUrl();

        return new DiaryListResponse(
                diary.getId(),
                diary.getContent(),
                diary.getEmotion(),
                diary.getWeather(),
                diary.getDiaryDate(),
                thumbnail,
                diary.getCreatedAt()
        );
    }
}

