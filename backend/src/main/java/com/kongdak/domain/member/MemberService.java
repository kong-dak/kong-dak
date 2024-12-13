package com.kongdak.domain.member;

import com.kongdak.controller.dto.MemberCreateRequest;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member createmember(MemberCreateRequest request) {
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
    public Member updateNickname(long memberId, String nickname) {
        Member member = findMemberById(memberId);
        member.updateNickname(nickname);
        return member;
    }

    @Transactional
    public void deactivateMember(Long memberId) {
        Member member = findMemberById(memberId);
        member.deactivate();
    }
}
