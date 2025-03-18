package com.kongdak.domain.dailyquestion.entity;


import com.kongdak.domain.BaseTimeEntity;
import com.kongdak.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "answer_emojis")
public class AnswerEmoji extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private DailyAnswer answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String emoji;

    @Builder
    public AnswerEmoji(DailyAnswer answer, Member member, String emoji) {
        this.answer = answer;
        this.member = member;
        this.emoji = emoji;
    }
}
