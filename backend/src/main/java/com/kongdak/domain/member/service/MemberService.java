package com.kongdak.domain.member.service;

import com.kongdak.domain.couple.entity.Couple;
import com.kongdak.domain.member.dto.request.MemberCreateRequest;
import com.kongdak.domain.member.dto.response.ActivateResponse;
import com.kongdak.domain.member.dto.response.DeactivateResponse;
import com.kongdak.domain.member.dto.response.MemberNicknameChangeResponse;
import com.kongdak.domain.member.dto.response.MemberResponse;
import com.kongdak.domain.member.repository.MemberRepository;
import com.kongdak.domain.member.entity.Member;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import com.kongdak.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SecurityUtil securityUtil;

    @Transactional
    public Member createMember(MemberCreateRequest request) {
        validateDuplicateEmail(request.email());

        Member member = Member.builder()
                .email(request.email())
                .nickname(request.nickname())
                .oAuthProvider(request.oauthProvider())
                .build();

        return memberRepository.save(member);
    }

    private void validateDuplicateEmail(String email) {
        if(memberRepository.existsByEmail(email)){
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public MemberNicknameChangeResponse updateNickname(String email, String nickname) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        member.setNicknameSet(true);
        member.updateNickname(nickname);

        return new MemberNicknameChangeResponse(email, nickname);
    }

    @Transactional
    public DeactivateResponse deactivateMember(String email) {
        Member member = findByEmail(email);
        member.deactivate();

        return new DeactivateResponse(
                email,
                LocalDateTime.now()
        );
    }

    @Transactional
    public ActivateResponse activateMember(String email) {
        Member member = findByEmail(email);
        member.activate();

        return new ActivateResponse(
                email,
                LocalDateTime.now()
        );
    }

    public Member getCurrentMember() {
        String email = securityUtil.getCurrentUserEmail();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo(String email) {
        Member member = findByEmail(email);
        Member partner = null;

        if (member.getCouple() != null) {
            partner = memberRepository.findByCouple(member.getCouple()).stream()
                    .filter(m -> !m.getId().equals(member.getId()))
                    .findFirst()
                    .orElse(null);
        }

        return MemberResponse.from(member, partner);
    }

    public Long getPartnerIdByMemberId(Long memberId){
        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Couple couple = member.getCouple();

        return memberRepository.findByCoupleAndIdNot(couple, memberId)
                               .map(Member::getId)
                               .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));
    }


}
