package com.kongdak.domain.dailyquestion;

import com.kongdak.controller.dto.request.DailyAnswerRequest;
import com.kongdak.controller.dto.request.EmojiRequest;
import com.kongdak.controller.dto.request.ReplyRequest;
import com.kongdak.controller.dto.response.DailyAnswerResponse;
import com.kongdak.controller.dto.response.DailyQuestionResponse;
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
                dailyAnswerRepository.findLastAnswerByMemberId(memberId);
        // 다음 질문 조회
        DailyQuestion nextQuestion;
        if (lastAnswer.isEmpty()) {
            // 첫 번째 질문 조회
            nextQuestion = dailyQuestionRepository.findFirstByOrderByIdAsc()
                    .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));
        } else {
            // 마지막 답변의 다음 질문 조회
            nextQuestion = dailyQuestionRepository.findNextQuestion(lastAnswer.get().getQuestion().getId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));
        }

        return DailyQuestionResponse.builder()
                .questionId(nextQuestion.getId())
                .title(nextQuestion.getTitle())
                .build();

    }

    // 답변 작성
    @Transactional
    public DailyAnswerResponse createAnswer(Long memberId, Long questionId, DailyAnswerRequest request) {
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
        return DailyAnswerResponse.from(savedAnswer);
    }

    // 답변 조회 (커플 둘 다 답변했을 때만 상대방 답변 보이도록)
    public List<DailyAnswerResponse> getAnswers(Long memberId, Long questionId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        log.info("[DailyQuestionService - getAnswers]- memberId : {}", memberId);
        // 커플 관계 확인
        Long coupleId = member.getCoupleId();
        log.info("[DailyQuestionService - getAnswers]- Couple 엔티티: {}", member.getCouple());
        log.info("[DailyQuestionService - getAnswers]- coupleId : {}", coupleId);
        if (coupleId == null) {
            throw new BusinessException(ErrorCode.COUPLE_NOT_FOUND);
        }

        List<DailyAnswer> answers = dailyAnswerRepository.findByQuestionId(questionId);

        // 둘 다 답변했는지 확인
        if (answers.size() == 2) {
            return answers.stream()
                    .map(DailyAnswerResponse::from)
                    .collect(Collectors.toList());
        }

        // 한 명만 답변했을 경우 자신의 답변만 반환
        return answers.stream()
                .filter(answer -> answer.getMember().getId().equals(memberId))
                .map(DailyAnswerResponse::from)
                .collect(Collectors.toList());
    }

    // 이모지 반응 추가
    @Transactional
    public void addEmoji(Long memberId, Long answerId, EmojiRequest request) {
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

        answerEmojiRepository.save(answerEmoji);
    }

    // 댓글 작성
    @Transactional
    public void addReply(Long memberId, Long questionId, ReplyRequest request) {
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
    }

    // 댓글 삭제
    @Transactional
    public void deleteReply(Long memberId, Long questionId, Long replyId) {
        AnswerReply reply = answerReplyRepository.findById(replyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPLY_NOT_FOUND));

        // 자신의 댓글만 삭제 가능
        if (!reply.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.NOT_YOUR_REPLY);
        }

        answerReplyRepository.delete(reply);
    }
}
