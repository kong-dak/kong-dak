package com.kongdak.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisPlaceRepository {

    private final StringRedisTemplate redisTemplate;

    public void savePlaceId(String key) {
        redisTemplate.opsForValue().set(key, "1");
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

}
