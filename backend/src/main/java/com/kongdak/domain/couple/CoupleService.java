package com.kongdak.domain.couple;

import com.kongdak.controller.dto.request.CoupleMatchRequest;
import com.kongdak.controller.dto.response.CoupleResponse;
import com.kongdak.domain.calendar.Calendar;
import com.kongdak.domain.calendar.CalendarRepository;
import com.kongdak.domain.member.Member;
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
import java.util.Random;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CoupleService {
    private final CoupleRepository coupleRepository;
    private final MemberService memberService;
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
    public void requestMatch(Long requesterId, String targetCode) {
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
    }


    // 매칭 수락
    @Transactional
    public void acceptMatch(String requestId, Long memberId) {
        log.info("[CoupleService - acceptMatch] - requestId : {} , memberId : {}", requestId, memberId);
        CoupleMatchRequest request = coupleMatchRedisRepository.findCoupleMatchRequest(requestId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPLE_MATCH_REQUEST_NOT_FOUND));

        if (!request.targetId().equals(memberId)) {
            throw new BusinessException(ErrorCode.CANNOT_MATCH_TO_OWN);
        }

        log.info("[CoupleService - acceptMatch] - request.requesterId : {} , memberId : {}", request.requesterId(), memberId);
        // 커플 연결 처리
        connect(request.requesterId(), memberId, LocalDateTime.now());

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
    }

    // 매칭 거절
    public void rejectMatch(String requestId, Long memberId) {
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
    }

    @Transactional
    public CoupleResponse connect(Long memberId, Long partnerId, LocalDateTime anniversaryDate) {

        Member member = memberService.findMemberById(memberId);
        Member partner = memberService.findMemberById(partnerId);

        validateConnection(member, partner);

        // 먼저 Couple을 저장
        Couple couple = coupleRepository.save(
                Couple.builder()
                        .member1(member)
                        .member2(partner)
                        .anniversaryDate(anniversaryDate)
                        .build()
        );

        // Calendar도 생성되어야 한다.
        Calendar calendar = Calendar.builder()
                .couple(couple)
                .build();
        calendarRepository.save(calendar);

        coupleRepository.save(couple);
        return CoupleResponse.from(couple, memberId);
    }

    @Transactional
    public void disconnect(Long coupleId, Long memberId) {
        Couple couple = findCoupleById(coupleId);
        validateMemberInCouple(couple, memberId);
        couple.disconnect();
    }

    @Transactional
    public void restore(Long coupleId, Long memberId) {
        Couple couple = findCoupleById(coupleId);
        validateMemberInCouple(couple, memberId);
        couple.restore();
    }

    public Couple findCoupleById(Long coupleId) {
        return coupleRepository.findById(coupleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPLE_NOT_FOUND));

    }

    private void validateConnection(Member member, Member partner) {
        if (coupleRepository.existsByMember1OrMember2(member, member)) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_COUPLED);
        }
        if (coupleRepository.existsByMember1OrMember2(partner, partner)) {
            throw new BusinessException(ErrorCode.PARTNER_ALREADY_COUPLED);
        }
    }

    private void validateMemberInCouple(Couple couple, Long memberId) {
        if (!couple.getMember1().getId().equals(memberId) &&
                !couple.getMember2().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.NOT_COUPLE_MEMBER);
        }
    }
}
