package com.kongdak.domain.map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<Place, Long> {
    boolean existsByPlaceId(String placeId);
}
