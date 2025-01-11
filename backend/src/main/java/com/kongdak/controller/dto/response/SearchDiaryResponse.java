package com.kongdak.controller.dto.response;

import com.kongdak.domain.diary.Diary;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "다이어리 검색 응답")
public record SearchDiaryResponse(
        @Schema(description = "다이어리 목록")
        List<DiaryListResponse> diaries,

        @Schema(description = "전체 페이지 수", example = "10")
        int totalPages,

        @Schema(description = "전체 항목 수", example = "100")
        long totalElements,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext
) {
    public static SearchDiaryResponse from(Page<Diary> diaryPage) {
        return new SearchDiaryResponse(
                diaryPage.getContent().stream()
                        .map(DiaryListResponse::from)
                        .toList(),
                diaryPage.getTotalPages(),
                diaryPage.getTotalElements(),
                diaryPage.hasNext()
        );
    }
}
