package com.kongdak.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String provider = oAuth2User.getProvider();
        String email = oAuth2User.getEmail();
        // Access Token & Refresh Token 생성
        String accessToken = jwtTokenProvider.createToken(oAuth2User);
        String refreshToken = jwtTokenProvider.createRefreshToken(oAuth2User);

        refreshTokenRepository.save(email, refreshToken,
                jwtTokenProvider.getRefreshTokenValidityInMilliseconds());
        // Response 설정
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        // TokenResponse DTO 생성 및 JSON 응답
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .provider(provider)
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));

        log.info("OAuth2 Login Success: {}", String.valueOf(email));
    }
}
