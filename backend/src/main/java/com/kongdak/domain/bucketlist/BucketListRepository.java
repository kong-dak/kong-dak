package com.kongdak.domain.bucketlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BucketListRepository extends JpaRepository<BucketList, Long> {
    List<BucketList> findByCoupleIdOrderByOrderNumAsc(Long coupleId);

    // 최대 orderNum 조회 메서드
    @Query("SELECT MAX(b.orderNum) FROM BucketList b WHERE b.couple.id = :coupleId")
    Optional<Integer> findMaxOrderByCoupleId(@Param("coupleId") Long coupleId);
}
