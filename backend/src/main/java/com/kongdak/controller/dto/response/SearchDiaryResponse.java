package com.kongdak.controller.dto.response;

import com.kongdak.domain.diary.Diary;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "다이어리 검색 응답")
public record SearchDiaryResponse(
        @Schema(description = "다이어리 목록")
        List<DiaryListResponse> diaries

) {
    public static SearchDiaryResponse from(List<Diary> diaries) {
        return new SearchDiaryResponse(
                diaries.stream()
                        .map(DiaryListResponse::from)
                        .toList()
        );
    }
}
