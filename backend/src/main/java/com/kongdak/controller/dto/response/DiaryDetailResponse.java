package com.kongdak.controller.dto.response;

import com.kongdak.domain.diary.Diary;
import com.kongdak.domain.diary.Emotion;
import com.kongdak.domain.diary.Weather;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DiaryDetailResponse(
        Long diaryId,
        String content,
        Emotion emotion,
        Weather weather,
        LocalDate diaryDate,
        List<PhotoResponse> photos,
        List<DecorationResponse> decorations,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isEditing,
        Long editorId
) {
    public static DiaryDetailResponse from(Diary diary) {
        return new DiaryDetailResponse(
                diary.getId(),
                diary.getContent(),
                diary.getEmotion(),
                diary.getWeather(),
                diary.getDiaryDate(),
                diary.getPhotos().stream()
                        .map(PhotoResponse::from)
                        .toList(),
                diary.getDecorations().stream()
                        .map(DecorationResponse::from)
                        .toList(),
                diary.getCreatedAt(),
                diary.getUpdatedAt(),
                diary.isEditing(),
                diary.getEditor() != null ? diary.getEditor().getId() : null
        );
    }
}
