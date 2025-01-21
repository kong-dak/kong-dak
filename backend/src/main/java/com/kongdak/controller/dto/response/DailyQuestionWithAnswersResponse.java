package com.kongdak.controller.dto.response;

import com.kongdak.domain.dailyquestion.DailyAnswer;
import com.kongdak.domain.dailyquestion.DailyQuestion;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record DailyQuestionWithAnswersResponse(
        Long questionId,
        String title,
        List<DailyAnswerResponse> answers,
        boolean bothAnswered
) {
    public static DailyQuestionWithAnswersResponse of(
            DailyQuestion question,
            List<DailyAnswer> answers,
            Long currentMemberId
    ) {
        return new DailyQuestionWithAnswersResponse(
                question.getId(),
                question.getTitle(),
                answers.stream()
                        .map(answer -> DailyAnswerResponse.from(answer, answers.size() == 2, currentMemberId))
                        .collect(Collectors.toList()),
                answers.size() == 2
        );
    }
}