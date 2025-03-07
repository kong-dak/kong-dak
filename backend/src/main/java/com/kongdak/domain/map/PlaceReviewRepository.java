package com.kongdak.domain.map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long> {

    @Query("SELECT r FROM PlaceReview r WHERE r.place.id = :placeId ORDER BY r.createdAt DESC")
    List<PlaceReview> findTop3ByPlaceId(Long placeId);

}
