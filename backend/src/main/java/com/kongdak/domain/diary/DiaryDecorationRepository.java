package com.kongdak.domain.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaryDecorationRepository extends JpaRepository<DiaryDecoration, Long> {
    List<DiaryDecoration> findByDiaryId(Long diaryId);

    @Query("SELECT dd FROM DiaryDecoration dd WHERE dd.diary.id = :diaryId AND dd.diary.couple.id = :coupleId")
    List<DiaryDecoration> findByDiaryIdAndCoupleId(@Param("diaryId") Long diaryId, @Param("coupleId") Long coupleId);

    void deleteByDiaryId(Long diaryId);
}
