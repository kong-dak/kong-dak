package com.kongdak.global.security.jwt.sdk;

import com.kongdak.domain.member.Member;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import com.kongdak.global.security.jwt.CustomUserDetails;
import com.kongdak.global.security.jwt.TokenPairResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SimpleJwtTokenProvider {
    private final Key key;
    private final long accessTokenValidityInMilliseconds;
    @Getter
    private final long refreshTokenValidityInMilliseconds;


    public SimpleJwtTokenProvider(
            @Value("${spring.jwt.secret}") String secretKey,
            @Value("${spring.jwt.access-token-validity}") long accessTokenValidityInSeconds,
            @Value("${spring.jwt.refresh-token-validity}") long refreshTokenValidityInSeconds) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
    }

    public TokenPairResponse createTokenPair(Member member) {
        String accessToken = createAccessToken(member);
        String refreshToken = createRefreshToken(member.getEmail(), member.getId());

        return new TokenPairResponse(accessToken, refreshToken, member.isNicknameSet());
    }

    private String createAccessToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(member.getEmail())
                .claim("id", member.getId())
                .claim("auth", "ROLE_USER")
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken(String email, Long id) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    // Claims 파싱
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // 토큰에서 Authentication 객체 추출
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        log.info("Token claims: {}", claims);
        log.info("id claim value: {}", claims.get("id"));
        log.info("subject value: {}", claims.getSubject());
        log.info("auth claim value: {}", claims.get("auth"));

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

    public TokenPairResponse reissueTokens(String oldRefreshToken) {
        // Refresh Token 검증
        if (!validateToken(oldRefreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // Refresh Token에서 사용자 정보(email) 추출
        Claims claims = parseClaims(oldRefreshToken);
        String email = claims.getSubject();
        Long id = Long.parseLong(claims.get("id").toString());  // id 추출

        // 3. 새로운 토큰 쌍 생성
        Date now = new Date();
        Date accessTokenValidity = new Date(now.getTime() + accessTokenValidityInMilliseconds);
        Date refreshTokenValidity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);


        // 새로운 Access Token 생성
        String newAccessToken = Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .claim("auth", "ROLE_USER")
                .setIssuedAt(now)
                .setExpiration(accessTokenValidity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // 새로운 Refresh Token 생성
        String newRefreshToken = Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .setIssuedAt(now)
                .setExpiration(refreshTokenValidity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new TokenPairResponse(newAccessToken, newRefreshToken, true);
    }
}
