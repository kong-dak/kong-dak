package com.kongdak.domain.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "다이어리 생성 응답")
public record DiaryCreateResponse(
        @Schema(description = "생성된 다이어리 ID", example = "1")
        Long diaryId
) {
    public static DiaryCreateResponse of(Long id) {
        return new DiaryCreateResponse(id);
    }
}
