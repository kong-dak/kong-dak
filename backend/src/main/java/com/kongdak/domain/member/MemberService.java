package com.kongdak.domain.member;

import com.kongdak.controller.dto.request.MemberCreateRequest;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import com.kongdak.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Member updateNickname(String email, String nickname) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND)
        );
        member.updateNickname(nickname);
        return member;
    }

    @Transactional
    public void deactivateMember(Long memberId) {
        Member member = findMemberById(memberId);
        member.deactivate();
    }

    public Member getCurrentMember() {
        String email = securityUtil.getCurrentUserEmail();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
