package com.kongdak.domain.diary;

import com.kongdak.controller.dto.request.CreateDiaryRequest;
import com.kongdak.controller.dto.request.DecorationUpdateRequest;
import com.kongdak.controller.dto.request.DiaryUpdateRequest;
import com.kongdak.controller.dto.response.DiaryDeleteResponse;
import com.kongdak.controller.dto.response.DiaryDetailResponse;
import com.kongdak.controller.dto.response.DiaryUpdateResponse;
import com.kongdak.controller.dto.response.SearchDiaryResponse;
import com.kongdak.domain.couple.CoupleRepository;
import com.kongdak.domain.member.Member;
import com.kongdak.domain.member.MemberRepository;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import com.kongdak.global.redis.RedisLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    public DiaryUpdateResponse updateDiary(Long memberId, Long diaryId, DiaryUpdateRequest request) {
        Diary diary = getDiaryByIdAndMemberId(diaryId, memberId);
        validateDiaryEditable(diary, memberId);

        try {
            Map<String, Object> changedFields = new HashMap<>();

            // 기본 필드들 변경 확인
            if (!diary.getContent().equals(request.content())) {
                changedFields.put("content", request.content());
            }
            if (!diary.getEmotion().equals(request.emotion())) {
                changedFields.put("emotion", request.emotion());
            }
            if (!diary.getWeather().equals(request.weather())) {
                changedFields.put("weather", request.weather());
            }

            // 기본 정보 업데이트
            diary.update(request.content(), request.emotion(), request.weather());

            // 사진 업데이트가 필요한지 확인
            List<String> currentPhotoUrls = diary.getPhotos().stream()
                    .map(DiaryPhoto::getPhotoUrl)
                    .toList();

            if (!currentPhotoUrls.equals(request.photoUrls())) {
                // 사진 목록이 변경됨
                updatePhotos(diary, request.photoUrls());
                Map<String, Object> photoInfo = new HashMap<>();
                photoInfo.put("urls", request.photoUrls());
                photoInfo.put("thumbnails", diary.getPhotos().stream()
                        .map(DiaryPhoto::getThumbnailUrl)
                        .collect(Collectors.toList()));
                changedFields.put("photos", photoInfo);
            }

            // 꾸미기 요소 업데이트가 필요한지 확인
            if (!compareDecorations(diary.getDecorations(), request.decorations())) {
                updateDecorations(diary, request.decorations());
                changedFields.put("decorations", request.decorations());
            }

            return DiaryUpdateResponse.of(
                    diaryId,
                    changedFields
            );
        } finally {
            releaseLock(diaryId, memberId);
        }
    }

    private boolean compareDecorations(List<DiaryDecoration> currentDecorations, List<DecorationUpdateRequest> requestDecorations) {
        if (currentDecorations.size() != requestDecorations.size()) {
            return false;
        }

        // 모든 속성이 동일한지 확인
        for (int i = 0; i < currentDecorations.size(); i++) {
            DiaryDecoration current = currentDecorations.get(i);
            DecorationUpdateRequest request = requestDecorations.get(i);

            if (!current.getType().equals(request.type()) ||
                    !Objects.equals(current.getContent(), request.content()) ||
                    !Objects.equals(current.getPositionX(), request.positionX()) ||
                    !Objects.equals(current.getPositionY(), request.positionY()) ||
                    !Objects.equals(current.getStyle(), request.style())) {
                return false;
            }
        }

        return true;
    }


    public DiaryDetailResponse getDiary(Long memberId, Long diaryId) {
        Couple couple = getCoupleByMemberId(memberId);
        Diary diary = getDiaryByIdAndCoupleId(diaryId, couple.getId());
        return DiaryDetailResponse.from(diary);
    }

    public SearchDiaryResponse searchDiaries(Long memberId, YearMonth dateTime) {
        Couple couple = getCoupleByMemberId(memberId);
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        List<Diary> diaries = diaryRepository.findMonthlyDiaries(couple.getId(), year, month);
        return SearchDiaryResponse.from(diaries);
    }

    @Transactional
    public DiaryDeleteResponse deleteDiary(Long memberId, Long diaryId) {
        Couple couple = getCoupleByMemberId(memberId);
        Diary diary = getDiaryByIdAndCoupleId(diaryId, couple.getId());
        validateDiaryEditable(diary, memberId);

        // 삭제 전에 응답 DTO 생성
        DiaryDeleteResponse response = DiaryDeleteResponse.of(diary);
        diaryRepository.delete(diary);

        return response;
    }

    @Transactional
    public boolean acquireLock(Long diaryId, Long memberId) {
        String lockKey = "diary:" + diaryId;
        return redisLockRepository.acquireLock(lockKey, memberId.toString(), LOCK_DURATION);
    }

    @Transactional
    public void releaseLock(Long diaryId, Long memberId) {
        String lockKey = "diary:" + diaryId;
        redisLockRepository.releaseLock(lockKey, memberId.toString());
    }

    // Private 헬퍼 메서드
    private void validateDiaryNotExistsForDate(Long coupleId, LocalDate date) {
        if (diaryRepository.existsByCoupleIdAndDiaryDate(coupleId, date)) {
            throw new BusinessException(ErrorCode.DIARY_ALREADY_EXISTS);
        }
    }

    private void validateDiaryEditable(Diary diary, Long memberId) {
        String lockKey = "diary:" + diary.getId();
        String lockHolder = redisLockRepository.getLockHolder(lockKey);
        if (lockHolder != null && !lockHolder.equals(memberId.toString())) {
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