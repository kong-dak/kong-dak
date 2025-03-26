package com.kongdak.domain.map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceOperatingHourRepository extends JpaRepository<PlaceOperatingHour, Long> {

    List<PlaceOperatingHour> findByPlaceId(Long placeId);

}
