package com.kongdak.domain.map;

import com.kongdak.domain.BaseTimeEntity;
import com.kongdak.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "place_reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", referencedColumnName = "place_id", nullable = false)
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private int rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @OneToMany(mappedBy = "placeReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceImage> placeImages = new ArrayList<>();

    @Builder
    public PlaceReview(Place place, Member member, int rating, String comment) {
        this.place = place;
        this.member = member;
        this.rating = rating;
        this.comment = comment;
    }

    public void updateReview(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public void addImage(PlaceImage image) {
        this.placeImages.add(image);
        image.setPlaceReview(this);
    }

    public void removeImage(PlaceImage image) {
        this.placeImages.remove(image);
        image.setPlaceReview(null);
    }

}
