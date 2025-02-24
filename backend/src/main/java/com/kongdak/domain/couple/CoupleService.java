package com.kongdak.domain.couple;

import com.kongdak.controller.dto.request.CoupleMatchRequest;
import com.kongdak.controller.dto.response.*;
import com.kongdak.domain.calendar.Calendar;
import com.kongdak.domain.calendar.CalendarRepository;
import com.kongdak.domain.member.Member;
import com.kongdak.domain.member.MemberRepository;
import com.kongdak.domain.member.MemberService;
import com.kongdak.domain.notification.NotificationMessage;
import com.kongdak.domain.notification.NotificationType;
import com.kongdak.domain.notification.SseEmitterService;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CoupleService {
    private final CoupleRepository coupleRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final CalendarRepository calendarRepository;
    private final CoupleMatchRedisRepository coupleMatchRedisRepository;
    private final SseEmitterService sseEmitterService;
    // 연결 코드 발급
    public String getConnectCode(Long memberId) {
        return coupleMatchRedisRepository.findCodeByMemberId(memberId)
                .orElseGet(() -> coupleMatchRedisRepository.saveConnectCode(memberId));
    }

    // 매칭 요청
    @Transactional
    public MatchRequestResponse requestMatch(Long requesterId, String targetCode) {
        Long targetId = coupleMatchRedisRepository.findMemberIdByCode(targetCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_MATCH_REQUEST_CODE));

        if (requesterId.equals(targetId)) {
            throw new BusinessException(ErrorCode.CANNOT_MATCH_TO_OWN);
        }
        String requestId = String.format("%06d", new Random().nextInt(1000000));
        coupleMatchRedisRepository.saveMatchRequest(requestId, requesterId, targetId);

        // SSE로 상대방에게 알림 전송
        NotificationMessage notification = new NotificationMessage(
                NotificationType.COUPLE_MATCH_REQUEST,
                requestId,
                requesterId
        );
        sseEmitterService.sendToMember(targetId, notification);

        return new MatchRequestResponse(
                requestId,
                requesterId,
                targetId,
                LocalDateTime.now()
        );
    }


    // 매칭 수락
    @Transactional
    public MatchAcceptResponse acceptMatch(String requestId, Long memberId) {
        CoupleMatchRequest request = coupleMatchRedisRepository.findCoupleMatchRequest(requestId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPLE_MATCH_REQUEST_NOT_FOUND));

        if (!request.targetId().equals(memberId)) {
            throw new BusinessException(ErrorCode.CANNOT_MATCH_TO_OWN);
        }
        LocalDateTime matchedAt = LocalDateTime.now();
        // 커플 연결 처리
        connect(request.requesterId(), memberId, matchedAt);

        // 매칭 요청 삭제
        coupleMatchRedisRepository.deleteMatchRequest(requestId);

//        // 양쪽 모두에게 매칭 성공 알림
        NotificationMessage notification = new NotificationMessage(
                NotificationType.COUPLE_MATCH_ACCEPTED,
                null,
                null
        );
        sseEmitterService.sendToMember(request.requesterId(), notification);
        sseEmitterService.sendToMember(memberId, notification);

        return MatchAcceptResponse.of(request.requesterId(), memberId, matchedAt);
    }

    // 매칭 거절
    public MatchRejectResponse rejectMatch(String requestId, Long memberId) {
        CoupleMatchRequest request = coupleMatchRedisRepository.findCoupleMatchRequest(requestId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPLE_MATCH_REQUEST_NOT_FOUND));

        if (!request.targetId().equals(memberId)) {
            throw new BusinessException(ErrorCode.CANNOT_MATCH_TO_OWN);
        }

        coupleMatchRedisRepository.deleteMatchRequest(requestId);

//        // 요청자에게 거절 알림
        NotificationMessage notification = new NotificationMessage(
                NotificationType.COUPLE_MATCH_REJECTED,
                null,
                null
        );
        sseEmitterService.sendToMember(request.requesterId(), notification);

        return MatchRejectResponse.of(
                request.requesterId(),
                memberId,
                LocalDateTime.now()
        );
    }

    @Transactional
    public CoupleResponse connect(Long memberId, Long partnerId, LocalDateTime anniversaryDate) {

        Member member = memberService.findMemberById(memberId);
        Member partner = memberService.findMemberById(partnerId);

        validateConnection(member, partner);

        // 먼저 Couple을 저장
        Couple couple = coupleRepository.save(
                Couple.builder()
                        .anniversaryDate(anniversaryDate)
                        .build()
        );
        member.setCouple(couple);
        partner.setCouple(couple);

        // Calendar도 생성되어야 한다.
        Calendar calendar = Calendar.builder()
                .couple(couple)
                .build();
        calendarRepository.save(calendar);

        return CoupleResponse.of(couple, partnerId);
    }

    @Transactional
    public CoupleDisconnectResponse disconnect(Long coupleId, Long memberId) {
        Couple couple = findCoupleById(coupleId);
        validateMemberInCouple(couple, memberId);

        LocalDateTime disconnectedAt = LocalDateTime.now();
        Long durationDays = ChronoUnit.DAYS.between(couple.getConnectedAt(), disconnectedAt);
        couple.disconnect();

        return CoupleDisconnectResponse.of(
                coupleId,
                memberId,
                disconnectedAt,
                durationDays
        );
    }

    @Transactional
    public CoupleRestoreResponse restore(Long coupleId, Long memberId) {
        Couple couple = findCoupleById(coupleId);
        validateMemberInCouple(couple, memberId);

        LocalDateTime restoredAt = LocalDateTime.now();
        LocalDateTime originalConnectedAt = couple.getConnectedAt();

        couple.restore();

        return CoupleRestoreResponse.of(
                coupleId,
                memberId,
                restoredAt,
                originalConnectedAt
        );
    }

    public Couple findCoupleById(Long coupleId) {
        return coupleRepository.findById(coupleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPLE_NOT_FOUND));

    }

    private void validateConnection(Member member, Member partner) {
        if (coupleRepository.existsByMemberIn(List.of(member, partner))) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_COUPLED);
        }
    }

    private void validateMemberInCouple(Couple couple, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (!couple.equals(member.getCouple())) {
            throw new BusinessException(ErrorCode.NOT_COUPLE_MEMBER);
        }
    }

    public boolean isCoupleMember(Member currentMember, Long coupleId) {
        return coupleRepository.findByMemberId(currentMember.getId()).orElseThrow(
                () -> new BusinessException(ErrorCode.COUPLE_NOT_FOUND)
        ).getId().equals(coupleId);
    }
}
