package com.kongdak.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;  // 추가
import org.springframework.security.core.authority.SimpleGrantedAuthority;  // 추가
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;  // 추가
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
public class JwtTokenProvider {

    private final Key key;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private final CustomOAuth2UserService customOAuth2UserService;


    public JwtTokenProvider(
            @Value("${spring.jwt.secret}") String secretKey,
            @Value("${spring.jwt.access-token-validity}") long accessTokenValidityInSeconds,
            @Value("${spring.jwt.refresh-token-validity}") long refreshTokenValidityInSeconds,
            CustomOAuth2UserService customOAuth2UserService) {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    // Access Token 생성
    public String createToken(OAuth2User oAuth2User) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(oAuth2User.getAttribute("email"))
                .claim("id", oAuth2User.getAttribute("id"))
                .claim("auth", oAuth2User.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(OAuth2User oAuth2User) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(oAuth2User.getAttribute("email"))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 Authentication 객체 추출
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        CustomUserDetails principal = CustomUserDetails.builder()
                .id(Long.parseLong(claims.get("id").toString()))
                .email(claims.getSubject())
                .authorities(authorities)
                .build();

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // Refresh Token으로부터 새로운 Access Token 생성
    public String refreshAccessToken(String refreshToken) {
        // Refresh Token 검증
        if (!validateToken(refreshToken)) {
            throw new JwtException("Invalid refresh token");
        }

        // Refresh Token에서 사용자 정보 추출
        Claims claims = parseClaims(refreshToken);
        String email = claims.getSubject();

        // CustomOAuth2UserService를 통해 사용자 정보를 다시 조회
        CustomOAuth2User oAuth2User = customOAuth2UserService.loadUserByEmail(email);

        return createToken(oAuth2User);
    }

    // Claims 파싱
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
