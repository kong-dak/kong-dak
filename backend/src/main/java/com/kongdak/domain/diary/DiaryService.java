package com.kongdak.domain.diary;

import com.kongdak.controller.dto.request.CreateDiaryRequest;
import com.kongdak.controller.dto.request.DecorationUpdateRequest;
import com.kongdak.controller.dto.request.UpdateDiaryRequest;
import com.kongdak.controller.dto.response.DiaryDetailResponse;
import com.kongdak.controller.dto.response.SearchDiaryResponse;
import com.kongdak.domain.couple.CoupleRepository;
import com.kongdak.domain.member.Member;
import com.kongdak.domain.member.MemberRepository;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import com.kongdak.global.redis.RedisLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.kongdak.domain.couple.Couple;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {
    private static final long LOCK_DURATION = TimeUnit.MINUTES.toMillis(30);

    private final DiaryRepository diaryRepository;
    private final DiaryPhotoRepository photoRepository;
    private final DiaryDecorationRepository decorationRepository;
    private final MemberRepository memberRepository;
    private final CoupleRepository coupleRepository;
    private final RedisLockRepository redisLockRepository;

    @Transactional
    public Long createDiary(Long memberId, CreateDiaryRequest request) {
        Member member = getMemberById(memberId);
        Couple couple = getCoupleByMemberId(memberId);

        // 해당 날짜에 이미 일기가 존재하는지 확인
        validateDiaryNotExistsForDate(couple.getId(), request.diaryDate());

        // 일기 생성
        Diary diary = Diary.builder()
                .couple(couple)
                .content(request.content())
                .emotion(request.emotion())
                .weather(request.weather())
                .diaryDate(request.diaryDate())
                .build();

        // 사진 추가
        if (request.photoUrls() != null) {
            request.photoUrls().forEach(url -> {
                DiaryPhoto photo = DiaryPhoto.builder()
                        .photoUrl(url)
                        .thumbnailUrl(generateThumbnail(url))
                        .build();
                diary.addPhoto(photo);
            });
        }

        // 꾸미기 요소 추가
        if (request.decorations() != null) {
            request.decorations().forEach(dec -> {
                DiaryDecoration decoration = DiaryDecoration.builder()
                        .type(dec.type())
                        .content(dec.content())
                        .positionX(dec.positionX())
                        .positionY(dec.positionY())
                        .style(dec.style())
                        .build();
                diary.addDecoration(decoration);
            });
        }

        return diaryRepository.save(diary).getId();
    }

    @Transactional
    public void updateDiary(Long memberId, Long diaryId, UpdateDiaryRequest request) {
        Diary diary = getDiaryByIdAndMemberId(diaryId, memberId);
        validateDiaryEditable(diary, memberId);

        try {
            // 기본 정보 업데이트
            diary.update(request.content(), request.emotion(), request.weather());

            // 사진 업데이트
            updatePhotos(diary, request.photoUrls());

            // 꾸미기 요소 업데이트
            updateDecorations(diary, request.decorations());
        } finally {
            // 편집 잠금 해제
            releaseLock(diaryId, memberId);
        }
    }

    public DiaryDetailResponse getDiary(Long memberId, Long diaryId) {
        Couple couple = getCoupleByMemberId(memberId);
        Diary diary = getDiaryByIdAndCoupleId(diaryId, couple.getId());
        return DiaryDetailResponse.from(diary);
    }

    public SearchDiaryResponse searchDiaries(Long memberId, Pageable pageable) {
        Couple couple = getCoupleByMemberId(memberId);
        Page<Diary> diaries = diaryRepository.findAllByCoupleIdOrderByDiaryDateDesc(couple.getId(), pageable);
        return SearchDiaryResponse.from(diaries);
    }

    @Transactional
    public void deleteDiary(Long memberId, Long diaryId) {
        Couple couple = getCoupleByMemberId(memberId);
        Diary diary = getDiaryByIdAndCoupleId(diaryId, couple.getId());
        validateDiaryEditable(diary, memberId);
        diaryRepository.delete(diary);
    }

    @Transactional
    public boolean acquireLock(Long diaryId, Long memberId) {
        String lockKey = "diary:" + diaryId;
        if (redisLockRepository.acquireLock(lockKey, memberId.toString(), LOCK_DURATION)) {
            Diary diary = getDiaryByIdAndMemberId(diaryId, memberId);
            diary.startEditing(getMemberById(memberId));
            return true;
        }
        return false;
    }

    @Transactional
    public void releaseLock(Long diaryId, Long memberId) {
        String lockKey = "diary:" + diaryId;
        if (redisLockRepository.releaseLock(lockKey, memberId.toString())) {
            Diary diary = getDiaryByIdAndMemberId(diaryId, memberId);
            diary.finishEditing();
        }
    }

    // Private 헬퍼 메서드
    private void validateDiaryNotExistsForDate(Long coupleId, LocalDate date) {
        if (diaryRepository.existsByCoupleIdAndDiaryDate(coupleId, date)) {
            throw new BusinessException(ErrorCode.DIARY_ALREADY_EXISTS);
        }
    }

    private void validateDiaryEditable(Diary diary, Long memberId) {
        if (diary.isEditing() && !memberId.equals(diary.getEditor().getId())) {
            throw new BusinessException(ErrorCode.DIARY_BEING_EDITED);
        }
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Couple getCoupleByMemberId(Long memberId) {
        return coupleRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPLE_NOT_FOUND));
    }

    private Diary getDiaryByIdAndMemberId(Long diaryId, Long memberId) {
        Couple couple = getCoupleByMemberId(memberId);
        return getDiaryByIdAndCoupleId(diaryId, couple.getId());
    }

    private Diary getDiaryByIdAndCoupleId(Long diaryId, Long coupleId) {
        return diaryRepository.findByIdAndCoupleId(diaryId, coupleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND));
    }

    private String generateThumbnail(String originalUrl) {
        // 실제 썸네일 생성 로직 구현 필요
        return originalUrl + "_thumbnail";
    }

    private void updatePhotos(Diary diary, List<String> newPhotoUrls) {
        if (newPhotoUrls == null) return;

        // 기존 사진 삭제
        diary.getPhotos().clear();

        // 새 사진 추가
        newPhotoUrls.forEach(url -> {
            DiaryPhoto photo = DiaryPhoto.builder()
                    .photoUrl(url)
                    .thumbnailUrl(generateThumbnail(url))
                    .build();
            diary.addPhoto(photo);
        });
    }

    private void updateDecorations(Diary diary, List<DecorationUpdateRequest> newDecorations) {
        if (newDecorations == null) return;

        // 기존 꾸미기 요소 삭제
        diary.getDecorations().clear();

        // 새 꾸미기 요소 추가
        newDecorations.forEach(dec -> {
            DiaryDecoration decoration = DiaryDecoration.builder()
                    .type(dec.type())
                    .content(dec.content())
                    .positionX(dec.positionX())
                    .positionY(dec.positionY())
                    .style(dec.style())
                    .build();
            diary.addDecoration(decoration);
        });
    }
}