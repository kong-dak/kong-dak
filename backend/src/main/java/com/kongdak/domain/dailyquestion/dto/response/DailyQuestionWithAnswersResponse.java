package com.kongdak.domain.dailyquestion.dto.response;

import com.kongdak.domain.dailyquestion.entity.DailyAnswer;
import com.kongdak.domain.dailyquestion.entity.DailyQuestion;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record DailyQuestionWithAnswersResponse(
        Long questionId,
        String title,
        List<DailyAnswerCreateResponse> answers,
        int replyCounts,
        boolean bothAnswered
) {
    public static DailyQuestionWithAnswersResponse of(
            DailyQuestion question,
            List<DailyAnswer> answers,
            Long currentMemberId,
            int replyCounts
    ) {
        return new DailyQuestionWithAnswersResponse(
                question.getId(),
                question.getTitle(),
                answers.stream()
                        .map(answer -> DailyAnswerCreateResponse.from(answer, answers.size() == 2, currentMemberId))
                        .collect(Collectors.toList()),
                replyCounts,
                answers.size() == 2
        );
    }
}