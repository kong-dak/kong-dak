package com.kongdak.domain.notification.repository;

import com.kongdak.domain.notification.entity.NotificationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEvent, Long> {
    // 사용자 알림 목록 조회 (생성 시간 기준 내림차순)
    List<NotificationEvent> findByReceiverIdOrderByTimestampDesc(Long receiverId);

    // 읽지 않은 알림 목록 조회 (생성 시간 기준 내림차순)
    List<NotificationEvent> findByReceiverIdAndIsReadFalseOrderByTimestampDesc(Long receiverId);

    // 특정 알림 조회 (ID와 수신자 ID로)
    Optional<NotificationEvent> findByIdAndReceiverId(Long id, Long receiverId);
}