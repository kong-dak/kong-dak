package com.kongdak.global.security.jwt;

import com.kongdak.domain.member.dto.request.MemberCreateRequest;
import com.kongdak.domain.member.entity.Member;
import com.kongdak.domain.member.repository.MemberRepository;
import com.kongdak.domain.member.service.MemberService;
import com.kongdak.domain.member.entity.OAuthProvider;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // 제공자(provider) 추출 (kakao or google)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = extractEmail(registrationId, attributes);

        // UUID로 임시 닉네임 생성
        String tempNickname = "User" + UUID.randomUUID().toString().substring(0, 8);

        // 회원가입 처리
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberService.createMember(new MemberCreateRequest(email, tempNickname, OAuthProvider.from(registrationId))));

        // 멤버가 비활성 상태인 경우 예외 처리
        if (!member.isActive()) {
            throw new OAuth2AuthenticationException("Inactive member");
        }

        return new CustomOAuth2User(
                member.getId(),
                oauth2User.getAuthorities(),
                attributes,
                registrationId,
                email
        );
    }

    private String extractEmail(String registrationId, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            return (String) kakaoAccount.get("email");
        } else if ("google".equals(registrationId)) {
            return (String) attributes.get("email");
        }
        throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
    }

    public CustomOAuth2User loadUserByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.isActive()) {
            throw new BusinessException(ErrorCode.INACTIVE_MEMBER);
        }

        Map<String, Object> attributes = Map.of(
                "email", member.getEmail(),
                "id", member.getId(),
                "nickname", member.getNickname(),
                "provider", member.getOauthProvider().toString()
        );

        return new CustomOAuth2User(
                member.getId(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                member.getOauthProvider().toString(),
                member.getEmail()
        );
    }
}
