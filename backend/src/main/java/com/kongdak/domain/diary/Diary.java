package com.kongdak.domain.diary;

import com.kongdak.domain.BaseTimeEntity;
import com.kongdak.domain.couple.Couple;
import com.kongdak.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "diaries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couple_id")
    private Couple couple;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Weather weather;

    @Column(nullable = false)
    private LocalDate diaryDate;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryPhoto> photos = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryDecoration> decorations = new ArrayList<>();

    @Builder
    public Diary(Couple couple, String content, Emotion emotion, Weather weather, LocalDate diaryDate) {
        this.couple = couple;
        this.content = content;
        this.emotion = emotion;
        this.weather = weather;
        this.diaryDate = diaryDate;
    }

    // 수정 메서드
    public void update(String content, Emotion emotion, Weather weather) {
        this.content = content;
        this.emotion = emotion;
        this.weather = weather;
    }

    // 사진 추가
    public void addPhoto(DiaryPhoto photo) {
        this.photos.add(photo);
        photo.setDiary(this);
    }

    // 꾸미기 추가
    public void addDecoration(DiaryDecoration decoration) {
        this.decorations.add(decoration);
        decoration.setDiary(this);
    }
}
