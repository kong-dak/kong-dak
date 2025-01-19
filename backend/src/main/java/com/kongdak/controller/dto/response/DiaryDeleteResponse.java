package com.kongdak.controller.dto.response;

import com.kongdak.domain.diary.Diary;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Schema(description = "다이어리 삭제 응답")
public record DiaryDeleteResponse(
        @Schema(description = "삭제된 다이어리 ID", example = "1")
        Long diaryId,

        @Schema(description = "커플 ID", example = "1")
        Long coupleId,

        @Schema(description = "다이어리 작성 날짜", example = "2024-01-19")
        LocalDate diaryDate,

        @Schema(description = "삭제 시간", example = "2024-01-19T18:30:00")
        LocalDateTime deletedAt
) {
    public static DiaryDeleteResponse of(Diary diary) {
        return new DiaryDeleteResponse(
                diary.getId(),
                diary.getCouple().getId(),
                diary.getDiaryDate(),
                LocalDateTime.now()
        );
    }
}