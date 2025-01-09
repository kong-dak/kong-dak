package com.kongdak.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY_PREFIX = "refreshToken:";

    public void save(String email, String refreshToken, long expirationTime) {
        redisTemplate.opsForValue()
                .set(KEY_PREFIX + email, refreshToken,
                        Duration.ofMillis(expirationTime));
    }

    public Optional<String> findByEmail(String email) {  // Optional로 감싸서 반환
        return Optional.ofNullable(redisTemplate.opsForValue().get(KEY_PREFIX + email));
    }

    public boolean deleteByEmail(String email) {  // 삭제 성공 여부 반환
        return Boolean.TRUE.equals(redisTemplate.delete(KEY_PREFIX + email));
    }

    public boolean hasKey(String email) {  // 키 존재 여부 확인
        return Boolean.TRUE.equals(redisTemplate.hasKey(KEY_PREFIX + email));
    }
}
