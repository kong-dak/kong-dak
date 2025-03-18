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
@Table(name = "answer_replies")  // API 명세서의 replies
public class AnswerReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private DailyQuestion question;

    @Column(nullable = false)
    private String content;

    @Builder
    public AnswerReply(Member member, DailyQuestion question, String content) {
        this.member = member;
        this.question = question;
        this.content = content;
    }
}
