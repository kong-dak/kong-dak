package com.kongdak.domain.dailyquestion.entity;

import com.kongdak.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_questions")
public class DailyQuestion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;     // 질문 내용

    @Column(nullable = false)
    private boolean isAnswered;  // 답변 여부

    @Builder
    public DailyQuestion(String title) {
        this.title = title;
        this.isAnswered = false;
    }
}

