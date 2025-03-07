package com.kongdak.domain.map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceImageRepository extends JpaRepository<PlaceImage, Long> {

    @Query("SELECT i FROM PlaceImage i WHERE i.place.id = :placeId ORDER BY i.id ASC LIMIT 3")
    List<PlaceImage> findTop3ByPlaceId(Long placeId);

}
