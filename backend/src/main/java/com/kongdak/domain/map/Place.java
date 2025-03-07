package com.kongdak.domain.map;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "places")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_id", unique = true, nullable = false)
    private Long placeId;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "address_name")
    private String addressName;

    @Column(name = "road_address_name")
    private String roadAddressName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "x")
    private String longitude;

    @Column(name = "y")
    private String latitude;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceReview> placeReviews = new ArrayList<>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceImage> placeImages = new ArrayList<>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceOperatingHour> operatingHours = new ArrayList<>();

    @Builder
    public Place(Long placeId, String placeName, String categoryName, String addressName,
                 String roadAddressName, String phone, String longitude, String latitude) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.categoryName = categoryName;
        this.addressName = addressName;
        this.roadAddressName = roadAddressName;
        this.phone = phone;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
