package com.kongdak.domain.map;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "place_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Column(nullable = false)
    private String imageUrl;

    @Column
    private String description;

    @Builder
    public PlaceImage(Place place, String imageUrl, String description) {
        this.place = place;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
