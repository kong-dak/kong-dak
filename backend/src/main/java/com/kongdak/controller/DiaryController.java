package com.kongdak.controller;

import com.kongdak.controller.dto.request.CreateDiaryRequest;
import com.kongdak.controller.dto.request.UpdateDiaryRequest;
import com.kongdak.controller.dto.response.DiaryCreateResponse;
import com.kongdak.controller.dto.response.DiaryDetailResponse;
import com.kongdak.controller.dto.response.SearchDiaryResponse;
import com.kongdak.domain.diary.DiaryService;
import com.kongdak.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
@Tag(name = "다이어리", description = "다이어리 관련 API")
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "다이어리 작성", description = "새로운 다이어리를 작성합니다.")
    @ApiResponse(responseCode = "200", description = "작성 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PostMapping
    public BaseResponse<DiaryCreateResponse> createDiary(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal Long memberId,
            @Parameter(description = "다이어리 작성 정보")
            @RequestBody @Valid CreateDiaryRequest request
    ) {
        return BaseResponse.created(
                DiaryCreateResponse.of(diaryService.createDiary(memberId, request))
        );
    }

    @Operation(summary = "다이어리 상세 조회", description = "특정 다이어리의 상세 내용을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/{diaryId}")
    public BaseResponse<DiaryDetailResponse> getDiary(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal Long memberId,
            @Parameter(description = "다이어리 ID", required = true)
            @PathVariable Long diaryId
    ) {
        return BaseResponse.ok(diaryService.getDiary(memberId, diaryId));
    }

    @Operation(summary = "다이어리 목록 조회", description = "다이어리 목록을 페이징하여 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping
    public BaseResponse<SearchDiaryResponse> searchDiaries(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal Long memberId,
            @Parameter(description = "페이징 정보")
            @PageableDefault Pageable pageable
    ) {
        SearchDiaryResponse response = diaryService.searchDiaries(memberId, pageable);
        return BaseResponse.ok(response);
    }

    @Operation(summary = "다이어리 수정", description = "작성된 다이어리를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PutMapping("/{diaryId}")
    public BaseResponse<Void> updateDiary(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal Long memberId,
            @Parameter(description = "다이어리 ID", required = true)
            @PathVariable Long diaryId,
            @Parameter(description = "다이어리 수정 정보")
            @RequestBody @Valid UpdateDiaryRequest request
    ) {
        diaryService.updateDiary(memberId, diaryId, request);
        return BaseResponse.ok();
    }

    @Operation(summary = "다이어리 삭제", description = "작성된 다이어리를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @DeleteMapping("/{diaryId}")
    public BaseResponse<Void> deleteDiary(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal Long memberId,
            @Parameter(description = "다이어리 ID", required = true)
            @PathVariable Long diaryId
    ) {
        diaryService.deleteDiary(memberId, diaryId);
        return BaseResponse.ok();
    }

    @Operation(summary = "다이어리 락 획득", description = "다이어리 편집을 위한 락을 획득합니다.")
    @ApiResponse(responseCode = "200", description = "락 획득 시도 결과",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PostMapping("/{diaryId}/lock")
    public BaseResponse<Boolean> acquireLock(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal Long memberId,
            @Parameter(description = "다이어리 ID", required = true)
            @PathVariable Long diaryId
    ) {
        return BaseResponse.ok(diaryService.acquireLock(diaryId, memberId));
    }

    @Operation(summary = "다이어리 락 해제", description = "획득한 다이어리 락을 해제합니다.")
    @ApiResponse(responseCode = "200", description = "락 해제 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @DeleteMapping("/{diaryId}/lock")
    public BaseResponse<Void> releaseLock(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal Long memberId,
            @Parameter(description = "다이어리 ID", required = true)
            @PathVariable Long diaryId
    ) {
        diaryService.releaseLock(diaryId, memberId);
        return BaseResponse.ok();
    }
}

