package com.kongdak.domain.dailyquestion.dto.response;

import com.kongdak.domain.dailyquestion.entity.AnswerEmoji;
import com.kongdak.domain.dailyquestion.entity.DailyAnswer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "데일리 수정 응답")
public record DailyAnswerUpdateResponse(

    @Schema(description = "답변 ID", example = "1")
    Long answerId,

    @Schema(description = "작성자 ID", example = "1")
    Long memberId,

    @Schema(description = "답변 내용", example = "오늘의 답변입니다.")
    String content,

    @Schema(description = "수정 시간", example = "2024-01-10T12:00:00")
    LocalDateTime updatedAt,

    @Schema(description = "현재 사용자가 볼 수 있는지 여부", example = "true")
    boolean isVisible,

    @Schema(description = "이모지", example = "true")
    List<String> emoji
)
    {
        public static DailyAnswerUpdateResponse from (DailyAnswer answer,boolean bothAnswered, Long currentMemberId){
        return new DailyAnswerUpdateResponse(
                answer.getId(),
                answer.getMember().getId(),
                answer.getContent(),
                answer.getUpdatedAt(),
                bothAnswered || answer.getMember().getId().equals(currentMemberId),
                answer.getEmojis().stream().map(
                        AnswerEmoji::getEmoji
                ).toList()
        );
    }

    }