package com.kongdak.domain.bucketlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BucketListRepository extends JpaRepository<BucketList, Long> {
    List<BucketList> findByCoupleIdOrderByOrderNumAsc(Long coupleId);

    Optional<Integer> findMinOrderByCoupleId(Long coupleId);
}
