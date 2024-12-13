package com.kongdak.domain.couple;

import com.kongdak.controller.dto.CoupleResponse;
import com.kongdak.domain.member.Member;
import com.kongdak.domain.member.MemberService;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CoupleService {
    private final CoupleRepository coupleRepository;
    private final MemberService memberService;

    @Transactional
    public CoupleResponse connect(Long memberId, Long partnerId, LocalDateTime anniversaryDate) {
        Member member = memberService.findMemberById(memberId);
        Member partner = memberService.findMemberById(partnerId);

        validateConnection(member, partner);

        Couple couple = Couple.builder()
                .member1(member)
                .member2(partner)
                .anniversaryDate(anniversaryDate)
                .build();

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
