package com.kongdak.domain.map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long> {

    Page<PlaceReview> findByPlace_PlaceId(Long placeId, Pageable pageable);
    Page<PlaceReview> findByMember_Id(Long memberId, Pageable pageable);

    Optional<PlaceReview> findByPlace_PlaceIdAndMember_Id(Long placeId, Long MemberId);

    @Query("SELECT r FROM PlaceReview r WHERE r.place.placeId = :placeId ORDER BY r.createdAt DESC")
    List<PlaceReview> findTop3ByPlaceId(Long placeId, Pageable pageable);

}
