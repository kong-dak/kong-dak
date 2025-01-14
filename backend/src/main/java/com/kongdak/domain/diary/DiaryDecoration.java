package com.kongdak.domain.diary;

import com.kongdak.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "diary_decorations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryDecoration extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DecorationType type;

    private String content;

    private Integer positionX;

    private Integer positionY;

    @Column(columnDefinition = "json")
    private String style;

    @Builder
    public DiaryDecoration(DecorationType type, String content,
                           Integer positionX, Integer positionY, String style) {
        this.type = type;
        this.content = content;
        this.positionX = positionX;
        this.positionY = positionY;
        this.style = style;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
    }
}
