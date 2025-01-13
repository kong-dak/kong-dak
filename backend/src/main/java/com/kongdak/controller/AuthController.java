package com.kongdak.controller;

import com.kongdak.controller.dto.request.TokenRefreshRequest;
import com.kongdak.controller.dto.response.TokenRefreshResponse;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import com.kongdak.global.response.BaseResponse;
import com.kongdak.global.security.jwt.CustomOAuth2UserService;
import com.kongdak.global.security.jwt.JwtTokenProvider;
import com.kongdak.global.security.jwt.RefreshTokenRepository;
import com.kongdak.global.security.jwt.TokenPairResponse;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "인증", description = "인증 관련 API")
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Operation(
            summary = "토큰 갱신",
            description = "Refresh 토큰을 사용하여 새로운 Access 토큰을 발급받습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "토큰 갱신 요청 처리 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = BaseResponse.class,
                                    subTypes = {TokenRefreshResponse.class}
                            )
                    )
            )
    })
    @PostMapping("/refresh")
    public BaseResponse<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        log.info("Received refresh token: {}", request.refreshToken());
        // RefreshToken 검증
        if (!jwtTokenProvider.validateToken(request.refreshToken())) {
            log.error("Token validation failed");
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 토큰에서 이메일 추출
        Claims claims = jwtTokenProvider.parseClaims(request.refreshToken());
        String email = claims.getSubject();
        log.info("Email from token: {}", email);

        // Redis에 저장된 RefreshToken 확인
        String savedToken = refreshTokenRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("No refresh token found in Redis for email: {}", email);
                return new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
                });

        log.info("Token from Redis: {}", savedToken);
        if (!savedToken.equals(request.refreshToken())) {
            log.error("Tokens do not match. Stored: {}, Received: {}",
                    savedToken.substring(0, 10) + "...",
                    request.refreshToken().substring(0, 10) + "...");
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // CustomOAuth2UserService를 통해 OAuth2User 정보 가져오기
        OAuth2User oAuth2User = customOAuth2UserService.loadUserByEmail(email);
        // 새로운 토큰 발급
        TokenPairResponse tokens = jwtTokenProvider.reissueTokens(request.refreshToken());

        // 새로운 RefreshToken을 Redis에 저장
        refreshTokenRepository.save(email, tokens.refreshToken(),
                jwtTokenProvider.getRefreshTokenValidityInMilliseconds());

        TokenRefreshResponse response = TokenRefreshResponse.builder()
                .accessToken(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .build();

        return BaseResponse.ok(response);
    }
}
