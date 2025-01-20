package com.kongdak.domain.couple;

import com.kongdak.controller.dto.request.CoupleMatchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CoupleMatchRedisRepository {
    private static final String CONNECT_CODE_PREFIX = "couple:connect:code:";
    private static final String MEMBER_CODE_PREFIX = "couple:member:code:";
    private static final String MATCH_REQUEST_PREFIX = "couple:match:request:";
    private static final Duration CODE_TTL = Duration.ofHours(24);

    private final RedisTemplate<String, String> redisTemplate;

    // 연결 코드 생성 및 저장
    public String saveConnectCode(Long memberId) {
        String code = generateConnectCode();

        // 기존 코드가 있는지 확인 후 있다면 삭제
        String existingCode = redisTemplate.opsForValue().get(MEMBER_CODE_PREFIX + memberId);
        if (existingCode != null) {
            redisTemplate.delete(CONNECT_CODE_PREFIX + existingCode);
        }

        // 새 코드 저장
        redisTemplate.opsForValue().set(CONNECT_CODE_PREFIX + code, memberId.toString(), CODE_TTL);
        redisTemplate.opsForValue().set(MEMBER_CODE_PREFIX + memberId, code, CODE_TTL);

        return code;
    }

    // memberId로 연결 코드 조회
    public Optional<String> findCodeByMemberId(Long memberId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(MEMBER_CODE_PREFIX + memberId));
    }

    // 연결 코드로 memberId 조회
    public Optional<Long> findMemberIdByCode(String code) {
        String memberId = redisTemplate.opsForValue().get(CONNECT_CODE_PREFIX + code);
        return Optional.ofNullable(memberId).map(Long::parseLong);
    }

    // 매칭 요청 저장
    public void saveMatchRequest(String requestId, Long requesterId, Long targetId) {
        String key = MATCH_REQUEST_PREFIX + requestId;
        String value = requesterId + ":" + targetId;
        redisTemplate.opsForValue().set(key, value, CODE_TTL);
    }

    // 매칭 요청 조회
    public Optional<CoupleMatchRequest> findCoupleMatchRequest(String requestId) {
        String value = redisTemplate.opsForValue().get(MATCH_REQUEST_PREFIX + requestId);
        log.info("[CoupleMatchRedisRepository-findCoupleMatchRequest] - MATCH_REQUEST_PREFIX + requestId : {}", MATCH_REQUEST_PREFIX + requestId);
        log.info("[CoupleMatchRedisRepository-findCoupleMatchRequest] - value : {}", value);
        if (value == null) {
            return Optional.empty();
        }

        String[] parts = value.split(":");
        log.info("[CoupleMatchRedisRepository-findCoupleMatchRequest] - parts[0] : {}", parts[0]);
        log.info("[CoupleMatchRedisRepository-findCoupleMatchRequest] - parts[1] : {}", parts[1]);
        return Optional.of(new CoupleMatchRequest(
                Long.parseLong(parts[0]),
                Long.parseLong(parts[1])
        ));
    }

    // 매칭 요청 삭제
    public void deleteMatchRequest(String requestId) {
        redisTemplate.delete(MATCH_REQUEST_PREFIX + requestId);
    }

    public String generateConnectCode() {
        String code;
        do {
            code = String.format("%06d", new Random().nextInt(1000000));
        } while (Boolean.TRUE.equals(redisTemplate.hasKey(CONNECT_CODE_PREFIX + code)));
        return code;
    }

    private String generateRequestCode() {
        String code;
        do {
            code = String.format("%06d", new Random().nextInt(1000000));
        } while (Boolean.TRUE.equals(redisTemplate.hasKey(MATCH_REQUEST_PREFIX + code)));
        return code;
    }


}
