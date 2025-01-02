package com.kongdak.controller.dto.response;

import com.kongdak.domain.diary.Diary;
import com.kongdak.domain.diary.Emotion;
import com.kongdak.domain.diary.Weather;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DiaryListResponse(
        Long diaryId,
        String content,
        Emotion emotion,
        Weather weather,
        LocalDate diaryDate,
        String thumbnailUrl,  // 리스트에서는 썸네일만 표시
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

