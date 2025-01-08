package com.kongdak.controller;

import com.kongdak.controller.dto.request.CreateDiaryRequest;
import com.kongdak.controller.dto.request.UpdateDiaryRequest;
import com.kongdak.controller.dto.response.DiaryDetailResponse;
import com.kongdak.controller.dto.response.SearchDiaryResponse;

import com.kongdak.domain.diary.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<Long> createDiary(
            @AuthenticationPrincipal Long memberId,
            @RequestBody CreateDiaryRequest request
    ) {
        Long diaryId = diaryService.createDiary(memberId, request);
        return ResponseEntity.ok(diaryId);
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryDetailResponse> getDiary(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long diaryId
    ) {
        DiaryDetailResponse response = diaryService.getDiary(memberId, diaryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<SearchDiaryResponse> searchDiaries(
            @AuthenticationPrincipal Long memberId,
            @PageableDefault Pageable pageable
    ) {
        SearchDiaryResponse response = diaryService.searchDiaries(memberId, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<Void> updateDiary(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long diaryId,
            @RequestBody UpdateDiaryRequest request
    ) {
        diaryService.updateDiary(memberId, diaryId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long diaryId
    ) {
        diaryService.deleteDiary(memberId, diaryId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{diaryId}/lock")
    public ResponseEntity<Boolean> acquireLock(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long diaryId
    ) {
        boolean acquired = diaryService.acquireLock(diaryId, memberId);
        return ResponseEntity.ok(acquired);
    }

    @DeleteMapping("/{diaryId}/lock")
    public ResponseEntity<Void> releaseLock(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long diaryId
    ) {
        diaryService.releaseLock(diaryId, memberId);
        return ResponseEntity.ok().build();
    }
}

