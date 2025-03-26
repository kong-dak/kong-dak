package com.kongdak.domain.map;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "place_operating_hours")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceOperatingHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", referencedColumnName = "place_id", nullable = false)
    private Place place;

    @Column(nullable = false)
    private String dayOfWeek;

    @Column(nullable = false)
    private String openTime;

    @Column(nullable = false)
    private String closeTime;

    @Builder
    public PlaceOperatingHour(Place place, String dayOfWeek, String openTime, String closeTime) {
        this.place = place;
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
