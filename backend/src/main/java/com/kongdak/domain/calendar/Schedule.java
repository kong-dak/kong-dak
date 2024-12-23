package com.kongdak.domain.calendar;

import com.kongdak.domain.BaseTimeEntity;
import com.kongdak.domain.member.Member;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name= "schedules")
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member creator;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleCategory category;

    private String emoji;

    private boolean isHoliday;

    private boolean isAnniversary;

    @Builder
    public Schedule(Calendar calendar, Member creator, String title,
                    LocalDateTime startTime, LocalDateTime endTime,
                    String description, ScheduleCategory category,
                    String emoji, boolean isHoliday, boolean isAnniversary) {
        this.calendar = calendar;
        this.creator = creator;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.category = category;
        this.emoji = emoji;
        this.isHoliday = isHoliday;
        this.isAnniversary = isAnniversary;
    }

    // 일정 수정
    public void update(String title, LocalDateTime startTime,
                       LocalDateTime endTime, String description,
                       ScheduleCategory category, String emoji) {
        validateSchedule(title, startTime, endTime);
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.category = category;
        this.emoji = emoji;
    }

    private void validateSchedule(String title, LocalDateTime startTime, LocalDateTime endTime) {
        if (title == null || title.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_TITLE);
        }
        if (title.length() > 30) {
            throw new BusinessException(ErrorCode.MAX_SCHEDULE_TITLE_LENGTH);
        }
        if (startTime.isAfter(endTime)) {
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_TIME);
        }
    }
}
