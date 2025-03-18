package com.kongdak.domain.diary.repository;

import com.kongdak.domain.diary.entity.DiaryPhoto;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryPhotoRepository extends JpaRepository<DiaryPhoto, Long> {
    List<DiaryPhoto> findByDiaryId(Long diaryId);

    @Query("SELECT dp FROM DiaryPhoto dp WHERE dp.diary.id = :diaryId AND dp.diary.couple.id = :coupleId")
    List<DiaryPhoto> findDiaryIdAndCoupleId(@Param("diaryId") Long diaryId, @Param("coupleId") Long coupleId);

    void deleteByDiaryId(Long diaryId);

}
