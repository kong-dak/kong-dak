package com.kongdak.domain.calendar;

import com.kongdak.domain.BaseTimeEntity;
import com.kongdak.domain.couple.Couple;
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
@Table(name = "calendars")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calendar extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "couple_id", nullable = false)
    private Couple couple;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    @Builder
    public Calendar(Couple couple, LocalDate anniversaryDate) {
        this.couple = couple;
    }
}
