package com.kongdak.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisLockRepository {
    private final StringRedisTemplate redisTemplate;

    public boolean acquireLock(String lockKey, String memberId, long timeoutMillis) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForValue()
                        .setIfAbsent(lockKey, memberId, timeoutMillis, TimeUnit.MILLISECONDS)
        );
    }

    public boolean releaseLock(String lockKey, String memberId) {
        String currentHolder = redisTemplate.opsForValue().get(lockKey);
        if (memberId.equals(currentHolder)) {
            return Boolean.TRUE.equals(redisTemplate.delete(lockKey));
        }
        return false;
    }

    public String getLockHolder(String lockKey) {
        return redisTemplate.opsForValue().get(lockKey);
    }

    public boolean isLocked(String lockKey) {
        return redisTemplate.opsForValue().get(lockKey) != null;
    }
}
