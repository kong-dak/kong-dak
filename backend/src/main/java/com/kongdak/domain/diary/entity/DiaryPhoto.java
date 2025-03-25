package com.kongdak.domain.diary.entity;

import com.kongdak.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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

    @Column(nullable = false, length = 1000)  // 길이를 1000으로 변경
    private String photoUrl;

    @Column(length = 1000)
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
