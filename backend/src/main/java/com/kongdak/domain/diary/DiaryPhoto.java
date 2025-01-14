package com.kongdak.domain.diary;

import com.kongdak.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "diary_photos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryPhoto extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Column(nullable = false)
    private String photoUrl;

    private String thumbnailUrl;

    @Builder
    public DiaryPhoto(String photoUrl, String thumbnailUrl) {
        this.photoUrl = photoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
    }
}
