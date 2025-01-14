package com.kongdak.domain.calendar;

import com.kongdak.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "holidays")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Holiday extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    private boolean isRecurringYearly;

    private String description;

    @Builder
    public Holiday(String name, LocalDate date,
                   boolean isRecurringYearly, String description) {
        this.name = name;
        this.date = date;
        this.isRecurringYearly = isRecurringYearly;
        this.description = description;
    }
}
