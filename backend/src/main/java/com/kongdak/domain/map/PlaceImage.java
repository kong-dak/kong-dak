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
    @JoinColumn(name = "review_id", nullable = false)
    private PlaceReview placeReview;

    @Column(nullable = false)
    private String imageUrl;

    @Column
    private String description;

    @Builder
    public PlaceImage(String imageUrl, String description) {
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public void setPlaceReview(PlaceReview review) {
        this.placeReview = review;
    }

}
