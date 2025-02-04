package com.kongdak.controller;

import com.kongdak.controller.dto.request.DailyAnswerRequest;
import com.kongdak.controller.dto.request.EmojiRequest;
import com.kongdak.controller.dto.request.ReplyRequest;
import com.kongdak.controller.dto.response.*;
import com.kongdak.domain.dailyquestion.DailyQuestionService;
import com.kongdak.global.response.BaseResponse;
import com.kongdak.global.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/daily-questions")
@Tag(name = "데일리 질문", description = "데일리 질문 관련 API")
public class DailyQuestionController {
    private final DailyQuestionService dailyQuestionService;

    @GetMapping("/current")
    @Operation(summary = "오늘의 질문과 답변 조회", description = "현재 진행 중인 데일리 질문과 해당 질문에 대한 답변들을 함께 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    public BaseResponse<DailyQuestionWithAnswersResponse> getDailyQuestion(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return BaseResponse.ok(dailyQuestionService.getDailyQuestionWithAnswers(userDetails.getId()));
    }

    @Operation(summary = "질문 및 답변 상세 조회", description = "특정 질문에 대한 제목과 답변을 상세 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/{questionId}")
    public BaseResponse<DailyQuestionWithAnswersResponse> getAnswers(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "질문 ID", required = true)
            @PathVariable("questionId") Long questionId) {
        return BaseResponse.ok(
                dailyQuestionService.getDailyQuestionDetail(userDetails.getId(), questionId)
        );
    }

    @Operation(summary = "답변 작성", description = "데일리 질문에 대한 답변을 작성합니다.")
    @ApiResponse(responseCode = "200", description = "작성 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PostMapping("/{questionId}/answers")
    public BaseResponse<DailyAnswerResponse> createAnswer(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "질문 ID", required = true)
            @PathVariable("questionId") Long questionId,
            @Parameter(description = "답변 내용")
            @RequestBody @Valid DailyAnswerRequest request) {
        return BaseResponse.created(dailyQuestionService.createAnswer(userDetails.getId(), questionId, request));
    }

    @Operation(summary = "댓글 조회", description = "데일리 질문의 댓글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/{questionId}/replies")
    public BaseResponse<List<AnswerReplyResponse>> getReplies(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "질문 ID", required = true)
            @PathVariable("questionId") Long questionId) {

        return BaseResponse.ok(dailyQuestionService.getReplies(questionId));
    }

    @Operation(summary = "질문 히스토리 조회", description = "첫 번째 질문부터 현재 진행 중인 질문까지의 모든 질문 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/history")
    public BaseResponse<List<DailyQuestionListResponse>> getAllQuestions(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return BaseResponse.ok(dailyQuestionService.getAllQuestions(userDetails.getId()));
    }

    @PatchMapping("/{questionId}/answers/{answerId}/emoji")
    @Operation(summary = "답변에 이모지 추가", description = "답변에 이모지 반응을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "이모지 추가 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    public BaseResponse<AnswerEmojiResponse> addEmoji(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "질문 ID", required = true)
            @PathVariable("questionId") Long questionId,
            @Parameter(description = "답변 ID", required = true)
            @PathVariable("answerId") Long answerId,
            @Parameter(description = "이모지 정보")
            @RequestBody @Valid EmojiRequest request) {

        return BaseResponse.ok(dailyQuestionService.addEmoji(userDetails.getId(), answerId, request));
    }

    @Operation(summary = "댓글 작성", description = "답변에 댓글을 작성합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 작성 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PostMapping("/{questionId}/replies")
    public BaseResponse<ReplyCreateResponse> addReply(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "질문 ID", required = true)
            @PathVariable("questionId") Long questionId,
            @Parameter(description = "댓글 내용")
            @RequestBody @Valid ReplyRequest request) {

        return BaseResponse.created(dailyQuestionService.addReply(userDetails.getId(), questionId, request));
    }

    @Operation(summary = "댓글 삭제", description = "작성한 댓글을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @DeleteMapping("/{questionId}/replies/{replyId}")
    public BaseResponse<ReplyDeleteResponse> deleteReply(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "질문 ID", required = true)
            @PathVariable("questionId") Long questionId,
            @Parameter(description = "댓글 ID", required = true)
            @PathVariable("replyId") Long replyId) {

        return BaseResponse.ok(dailyQuestionService.deleteReply(userDetails.getId(), questionId, replyId));
    }
}
