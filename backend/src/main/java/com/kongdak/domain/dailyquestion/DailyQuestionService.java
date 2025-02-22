package com.kongdak.domain.dailyquestion;

import com.kongdak.controller.dto.request.DailyAnswerRequest;
import com.kongdak.controller.dto.request.DailyAnswerUpdateRequest;
import com.kongdak.controller.dto.request.EmojiRequest;
import com.kongdak.controller.dto.request.ReplyRequest;
import com.kongdak.controller.dto.response.*;
import com.kongdak.domain.couple.Couple;
import com.kongdak.domain.couple.CoupleRepository;
import com.kongdak.domain.member.Member;
import com.kongdak.domain.member.MemberRepository;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyQuestionService {
    private final DailyQuestionRepository dailyQuestionRepository;
    private final DailyAnswerRepository dailyAnswerRepository;
    private final AnswerReplyRepository answerReplyRepository;
    private final AnswerEmojiRepository answerEmojiRepository;
    private final MemberRepository memberRepository;
    private final CoupleRepository coupleRepository;

    // 오늘의 질문 조회
    public DailyQuestionResponse getDailyQuestion(Long memberId) {

        // 현재 회원의 커플 정보 조회
        Couple couple = coupleRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPLE_NOT_FOUND));

        // 현재 회원의 마지막 답변 조회
        Optional<DailyAnswer> lastAnswer =
                dailyAnswerRepository.findFirstByMemberIdOrderByQuestionIdDesc(memberId);
        // 다음 질문 조회
        DailyQuestion nextQuestion;
        if (lastAnswer.isEmpty()) {
            // 첫 번째 질문 조회
            nextQuestion = dailyQuestionRepository.findFirstByOrderByIdAsc()
                    .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));
        } else {
            // 마지막 답변의 다음 질문 조회
            nextQuestion = dailyQuestionRepository.findFirstByIdGreaterThanOrderByIdAsc(lastAnswer.get().getQuestion().getId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));
        }

        return DailyQuestionResponse.builder()
                .questionId(nextQuestion.getId())
                .title(nextQuestion.getTitle())
                .build();

    }

    // 답변 작성
    @Transactional
    public DailyAnswerCreateResponse createAnswer(Long memberId, Long questionId, DailyAnswerRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        DailyQuestion question = dailyQuestionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        // 이미 답변했는지 확인
        if (dailyAnswerRepository.findByQuestionIdAndMemberId(questionId, memberId).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_ANSWERED);
        }

        DailyAnswer answer = DailyAnswer.builder()
                .question(question)
                .member(member)
                .content(request.content())
                .build();

        DailyAnswer savedAnswer = dailyAnswerRepository.save(answer);

        // 현재 질문에 대한 전체 답변 수 확인
        List<DailyAnswer> answers = dailyAnswerRepository.findByQuestionId(questionId);
        boolean bothAnswered = answers.size() == 2;

        return DailyAnswerCreateResponse.from(savedAnswer, bothAnswered, memberId);
    }

    // 답변 조회 (커플 둘 다 답변했을 때만 상대방 답변 보이도록)
    public DailyQuestionWithAnswersResponse getDailyQuestionDetail(Long memberId, Long questionId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 커플 관계 확인
        Long coupleId = member.getCoupleId();
        if (coupleId == null) {
            throw new BusinessException(ErrorCode.COUPLE_NOT_FOUND);
        }

        // 질문 조회
        DailyQuestion question = dailyQuestionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));
        List<DailyAnswer> answers = dailyAnswerRepository.findByQuestionId(questionId);
        int replyCounts = answerReplyRepository.countByQuestionId(questionId);
        boolean bothAnswered = answers.size() == 2;

        return DailyQuestionWithAnswersResponse.builder()
                .questionId(question.getId())
                .title(question.getTitle())
                .answers(answers.stream()
                        .map(answer -> DailyAnswerCreateResponse.from(answer, bothAnswered, memberId))
                        .collect(Collectors.toList()))
                .bothAnswered(bothAnswered)
                .replyCounts(replyCounts)
                .build();
    }

    // 이모지 반응 추가
    @Transactional
    public AnswerEmojiResponse addEmoji(Long memberId, Long answerId, EmojiRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        DailyAnswer answer = dailyAnswerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ANSWER_NOT_FOUND));

        // 자신의 답변에는 이모지를 달 수 없음
        if (answer.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.CANNOT_REACT_TO_OWN_ANSWER);
        }

        AnswerEmoji answerEmoji = AnswerEmoji.builder()
                .answer(answer)
                .member(member)
                .emoji(request.emoji())
                .build();

        AnswerEmoji savedEmoji = answerEmojiRepository.save(answerEmoji);
        return AnswerEmojiResponse.from(savedEmoji);
    }

    // 댓글 작성
    @Transactional
    public ReplyCreateResponse addReply(Long memberId, Long questionId, ReplyRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        DailyQuestion question = dailyQuestionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        // 둘 다 답변했는지 확인
        List<DailyAnswer> answers = dailyAnswerRepository.findByQuestionId(questionId);
        if (answers.size() != 2) {
            throw new BusinessException(ErrorCode.BOTH_ANSWERS_REQUIRED);
        }

        AnswerReply reply = AnswerReply.builder()
                .member(member)
                .question(question)
                .content(request.content())
                .build();

        answerReplyRepository.save(reply);

        return ReplyCreateResponse.from(reply);
    }

    // 댓글 삭제
    @Transactional
    public ReplyDeleteResponse deleteReply(Long memberId, Long questionId, Long replyId) {
        AnswerReply reply = answerReplyRepository.findById(replyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPLY_NOT_FOUND));

        // 자신의 댓글만 삭제 가능
        if (!reply.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.NOT_YOUR_REPLY);
        }
        ReplyDeleteResponse response = ReplyDeleteResponse.of(reply);
        answerReplyRepository.delete(reply);

        return response;
    }

    public DailyQuestionWithAnswersResponse getDailyQuestionWithAnswers(Long memberId) {
        // 현재 회원의 커플 정보 조회
        Couple couple = coupleRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPLE_NOT_FOUND));

        // 마지막 답변 조회
        Optional<DailyAnswer> lastAnswer = dailyAnswerRepository.findFirstByMemberIdOrderByQuestionIdDesc(memberId);

        // 현재/다음 질문 결정
        DailyQuestion question;
        if (lastAnswer.isEmpty()) {
            question = dailyQuestionRepository.findFirstByOrderByIdAsc()
                    .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));
        } else {
            DailyQuestion currentQuestion = lastAnswer.get().getQuestion();
            long answerCount = dailyAnswerRepository.countByQuestionIdAndCoupleId(
                    currentQuestion.getId(),
                    couple.getId()
            );

            question = (answerCount < 2)
                    ? currentQuestion
                    : dailyQuestionRepository.findFirstByIdGreaterThanOrderByIdAsc(currentQuestion.getId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));
        }

        // 질문에 대한 답변들 조회
        List<DailyAnswer> answers = dailyAnswerRepository.findByQuestionId(question.getId());
        int replyCounts = answerReplyRepository.countByQuestionId(question.getId());
        return DailyQuestionWithAnswersResponse.of(question, answers, memberId, replyCounts);
    }

    public List<DailyQuestionListResponse> getAllQuestions(Long memberId) {
        Long todayQuestionId = getDailyQuestion(memberId).questionId();

        // Id 1번부터 todayQuestionId까지 가져오는 메서드
        return dailyQuestionRepository.findByIdLessThanEqualOrderByIdDesc(todayQuestionId)
                .stream()
                .map(DailyQuestionListResponse::from)
                .collect(Collectors.toList());
    }


    public List<AnswerReplyResponse> getReplies(Long questionId) {

        List<AnswerReply> replies = answerReplyRepository.findByQuestionIdOrderByCreatedAtDesc(questionId);

        return replies.stream()
                .map(AnswerReplyResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public DailyAnswerUpdateResponse updateAnswer(Long memberId, Long questionId, Long answerId, DailyAnswerUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        DailyQuestion question = dailyQuestionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        DailyAnswer answer = dailyAnswerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ANSWER_NOT_FOUND));

        // 답변 작성자와 수정 요청자가 같은지 확인
        if (!answer.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.NOT_YOUR_ANSWER);
        }

        // 답변이 해당 질문에 대한 것인지 확인
        if (!answer.getQuestion().getId().equals(questionId)) {
            throw new BusinessException(ErrorCode.ANSWER_QUESTION_NOT_MATCH);
        }

        answer.updateContent(request.content());

        // 현재 질문에 대한 전체 답변 수 확인
        List<DailyAnswer> answers = dailyAnswerRepository.findByQuestionId(questionId);
        boolean bothAnswered = answers.size() == 2;

        return DailyAnswerUpdateResponse.from(answer, bothAnswered, memberId);
    }
}
