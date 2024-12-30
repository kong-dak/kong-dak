package com.kongdak.controller;

import com.kongdak.controller.dto.request.DailyAnswerRequestDto;
import com.kongdak.controller.dto.request.EmojiRequestDto;
import com.kongdak.controller.dto.request.ReplyRequestDto;
import com.kongdak.controller.dto.response.DailyAnswerResponseDto;
import com.kongdak.controller.dto.response.DailyQuestionResponseDto;
import com.kongdak.domain.dailyquestion.DailyQuestionService;
import com.kongdak.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/daily-question")
public class DailyQuestionController {
    private final DailyQuestionService dailyQuestionService;

    @GetMapping
    public ResponseEntity<ApiResponse<DailyQuestionResponseDto>> getDailyQuestion() {
        return ResponseEntity.ok(ApiResponse.ok(dailyQuestionService.getDailyQuestion()));
    }

    @GetMapping("/{questionId}/answers")
    public ResponseEntity<ApiResponse<List<DailyAnswerResponseDto>>> getAnswers(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long questionId) {
        return ResponseEntity.ok(ApiResponse.ok(
                dailyQuestionService.getAnswers(memberId, questionId)
        ));
    }

    @PostMapping("/{questionId}/answers")
    public ResponseEntity<ApiResponse<DailyAnswerResponseDto>> createAnswer(
            @AuthenticationPrincipal Long memberId,  // 인증된 사용자 ID
            @PathVariable Long questionId,
            @RequestBody @Valid DailyAnswerRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(dailyQuestionService.createAnswer(memberId, questionId, request)));
    }

    @PatchMapping("/{questionId}/answers/{answerId}/emoji")
    public ResponseEntity<ApiResponse<Void>> addEmoji(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long questionId,
            @PathVariable Long answerId,
            @RequestBody @Valid EmojiRequestDto request) {
        dailyQuestionService.addEmoji(memberId, answerId, request);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PostMapping("/{questionId}/replies")
    public ResponseEntity<ApiResponse<Void>> addReply(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long questionId,
            @RequestBody @Valid ReplyRequestDto request) {
        dailyQuestionService.addReply(memberId, questionId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created());
    }

    @DeleteMapping("/{questionId}/replies/{replyId}")
    public ResponseEntity<ApiResponse<Void>> deleteReply(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long questionId,
            @PathVariable Long replyId) {
        dailyQuestionService.deleteReply(memberId, questionId, replyId);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
