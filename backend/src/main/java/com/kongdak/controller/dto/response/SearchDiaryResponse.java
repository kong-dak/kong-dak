package com.kongdak.controller.dto.response;

import com.kongdak.domain.diary.Diary;
import org.springframework.data.domain.Page;

import java.util.List;

public record SearchDiaryResponse(
        List<DiaryListResponse> diaries,
        int totalPages,
        long totalElements,
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
