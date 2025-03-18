package com.kongdak.domain.diary.repository;

import com.kongdak.domain.couple.entity.Couple;

import com.kongdak.domain.diary.entity.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    // 특정 날짜의 일기 존재 여부 확인
    boolean existsByCoupleIdAndDiaryDate(Long coupleId, LocalDate diaryDate);

    Optional<Diary> findByIdAndCoupleId(Long id, Long coupleId);
    // 특정 달의 일기 목록 조회 (페이징)
    @Query("SELECT d FROM Diary d WHERE d.couple = :couple " +
            "AND YEAR(d.diaryDate) = :year " +
            "AND MONTH(d.diaryDate) = :month " +
            "ORDER BY d.diaryDate DESC")
    Page<Diary> findByYearAndMonth(
            @Param("couple") Couple couple,
            @Param("year") int year,
            @Param("month") int month,
            Pageable pageable
    );

    // 내용 검색 (페이징)
    @Query("SELECT d FROM Diary d WHERE d.couple = :couple " +
            "AND d.content LIKE %:keyword% " +
            "ORDER BY d.diaryDate DESC")
    Page<Diary> searchByContent(
            @Param("couple") Couple couple,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // 전체 일기 목록 조회 (페이징)
    Page<Diary> findAllByCoupleIdOrderByDiaryDateDesc(Long coupleId, Pageable pageable);

    // 특정 날짜의 일기 조회
    Optional<Diary> findByCoupleIdAndDiaryDate(Long coupleId, LocalDate diaryDate);

    @Query("SELECT d FROM Diary d WHERE d.couple.id = :coupleId " +
            "AND FUNCTION('YEAR', d.diaryDate) = :year " +
            "AND FUNCTION('MONTH', d.diaryDate) = :month " +
            "ORDER BY d.diaryDate ASC")
    List<Diary> findMonthlyDiaries(
            @Param("coupleId") Long coupleId,
            @Param("year") int year,
            @Param("month")int month)
            ;
}
