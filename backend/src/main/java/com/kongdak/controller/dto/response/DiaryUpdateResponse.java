package com.kongdak.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@Schema(description = "다이어리 수정 응답")
public record DiaryUpdateResponse(
        @Schema(description = "다이어리 ID", example = "1")
        Long diaryId,

        @Schema(description = "변경된 필드 목록")
        Map<String, Object> changedFields,

        @Schema(description = "수정 시간", example = "2024-01-19T18:30:00")
        LocalDateTime updatedAt
) {
    public static DiaryUpdateResponse of(Long diaryId, Map<String, Object> changedFields) {
        return new DiaryUpdateResponse(
                diaryId,
                changedFields,
                LocalDateTime.now()
        );
    }
}