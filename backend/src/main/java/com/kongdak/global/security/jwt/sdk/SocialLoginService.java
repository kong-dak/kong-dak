package com.kongdak.global.security.jwt.sdk;

import com.kongdak.controller.dto.request.MemberCreateRequest;
import com.kongdak.domain.member.Member;
import com.kongdak.domain.member.MemberRepository;
import com.kongdak.domain.member.MemberService;
import com.kongdak.domain.member.OAuthProvider;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import com.kongdak.global.security.jwt.RefreshTokenRepository;
import com.kongdak.global.security.jwt.TokenPairResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final SimpleJwtTokenProvider simplejwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final  String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    public TokenPairResponse socialLogin(String accessToken, String provider) {
        // 카카오 엑세스 토큰으로 사용자 정보 조회
        String email = validateKakaoTokenAndGetEmail(accessToken);

        // 이메일로 회원 조회 또는 가입 처리
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberService.createMember(
                        new MemberCreateRequest(
                                email,
                                generateTempNickname(),
                                OAuthProvider.from(provider)
                        )
                ));

        if (!member.isActive()) {
            throw new BusinessException(ErrorCode.INACTIVE_MEMBER);
        }

        // JWT 토큰 발급
        TokenPairResponse tokenPair = simplejwtTokenProvider.createTokenPair(member);

        // RefreshToken Redis 저장
        refreshTokenRepository.save(email, tokenPair.refreshToken(),
                simplejwtTokenProvider.getRefreshTokenValidityInMilliseconds());

        return tokenPair;

    }

    private String validateKakaoTokenAndGetEmail(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            log.info("카카오 user info 요청 with access token: {}", accessToken);
            ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                    KAKAO_USER_INFO_URI,
                    HttpMethod.POST,
                    entity,
                    KakaoUserInfo.class
            );

            log.info("Kakao API 요청 성공");
            KakaoUserInfo userInfo = response.getBody();
            if (userInfo == null) {
                log.error("Kakao API - null response body");
                throw new BusinessException(ErrorCode.INVALID_SOCIAL_TOKEN);
            }

            if (userInfo.kakaoAccount() == null) {
                log.error("Kakao user info에 kakao account detail 없음: {}", userInfo);
                throw new BusinessException(ErrorCode.INVALID_SOCIAL_TOKEN);
            }

            if (userInfo.kakaoAccount().email() == null) {
                log.error("Kakao account email 정보 없음: {}", userInfo.kakaoAccount());
                throw new BusinessException(ErrorCode.INVALID_SOCIAL_TOKEN);
            }

            log.info("Successfully validated Kakao token and retrieved email for user");
            return userInfo.kakaoAccount().email();
        } catch (HttpClientErrorException.Unauthorized e) {
            log.error("Unauthorized: Kakao token validation failed: {}", e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("expired")) {
                throw new BusinessException(ErrorCode.EXPIRED_SOCIAL_TOKEN);
            }
            throw new BusinessException(ErrorCode.INVALID_SOCIAL_TOKEN);
        } catch (HttpClientErrorException e) {
            log.error("HTTP error during Kakao token validation: Status: {}, Response: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new BusinessException(ErrorCode.INVALID_SOCIAL_TOKEN);
        } catch (RestClientException e) {
            log.error("Error during Kakao token validation: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SOCIAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error during Kakao token validation: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String generateTempNickname() {
        return "User" + UUID.randomUUID().toString().substring(0, 8);
    }
}
