package com.kongdak.domain.diary.dto.response;

import com.kongdak.domain.calendar.dto.response.PhotoResponse;
import com.kongdak.domain.diary.entity.Diary;
import com.kongdak.domain.diary.entity.Emotion;
import com.kongdak.domain.diary.entity.Weather;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "다이어리 상세 응답")
public record DiaryDetailResponse(
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

        @Schema(description = "첨부된 사진 목록")
        List<PhotoResponse> photos,

        @Schema(description = "적용된 데코레이션 목록")
        List<DecorationResponse> decorations,

        @Schema(description = "작성 시간", example = "2024-01-10T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "수정 시간", example = "2024-01-10T12:30:00")
        LocalDateTime updatedAt

) {

    public static DiaryDetailResponse from(Diary diary, List<PhotoResponse> photos) {
        return new DiaryDetailResponse(
                diary.getId(),
                diary.getContent(),
                diary.getEmotion(),
                diary.getWeather(),
                diary.getDiaryDate(),
                photos,
                diary.getDecorations().stream()
                        .map(DecorationResponse::from)
                        .toList(),
                diary.getCreatedAt(),
                diary.getUpdatedAt()
        );
    }
}
