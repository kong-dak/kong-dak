package com.kongdak.domain.bucketlist.service;

import com.kongdak.domain.bucketlist.dto.request.BucketListCreateDto;
import com.kongdak.domain.bucketlist.dto.request.BucketListUpdateDto;
import com.kongdak.domain.bucketlist.dto.response.BucketListResponseDto;
import com.kongdak.domain.bucketlist.entity.BucketList;
import com.kongdak.domain.bucketlist.repository.BucketListRepository;
import com.kongdak.domain.couple.entity.Couple;
import com.kongdak.domain.couple.service.CoupleService;
import com.kongdak.domain.member.service.MemberService;
import com.kongdak.domain.notification.service.NotificationService;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BucketListService {
    private final BucketListRepository bucketListRepository;
    private final CoupleService coupleService;
    private final MemberService memberService;
    private final NotificationService notificationService;
    public List<BucketListResponseDto> getBucketLists(Long memberId) {
        Couple couple = coupleService.findCoupleByMemberId(memberId);
        List<BucketList> bucketLists = bucketListRepository.findByCoupleIdOrderByOrderNumAsc(couple.getId());

        return bucketLists.stream()
                .map(BucketListResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public BucketListResponseDto createBucketList(Long memberId, BucketListCreateDto dto){
        Couple couple = coupleService.findCoupleByMemberId(memberId);

        // 새 항목의 순서 결정 (가장 큰 orderNum +1). 즉, 가장 아래에 위치
        int newOrder = bucketListRepository.findMaxOrderByCoupleId(couple.getId())
                .map(maxOrder -> maxOrder + 1)
                .orElse(0);

        BucketList bucketList = BucketList.builder()
                .couple(couple)
                .title(dto.title())
                .category(dto.category())
                .orderNum(newOrder)
                .build();

        Long partnerId = memberService.getPartnerIdByMemberId(memberId);
        notificationService.sendBucketCreatedNotification(bucketList.getId(), memberId, partnerId);
        BucketList savedBucketList = bucketListRepository.save(bucketList);
        return BucketListResponseDto.from(savedBucketList);
    }

    @Transactional
    public BucketListResponseDto updateBucketList(Long memberId, Long bucketListId, BucketListUpdateDto dto){
        BucketList bucketList = findBucketListAndValidateAccess(memberId, bucketListId);

        if (dto.title() != null) {
            bucketList.updateTitle(dto.title());
        }

        if (dto.isCompleted() != null) {
            bucketList.toggleCompletion();
        }
        if (bucketList.isCompleted()){
            Long partnerId = memberService.getPartnerIdByMemberId(memberId);
            notificationService.sendBucketCompletedNotification(bucketListId, memberId, partnerId);
        }

        return BucketListResponseDto.from(bucketList);
    }

    @Transactional
    public void deleteBucketList(Long memberId, Long bucketListId) {
        BucketList bucketList = findBucketListAndValidateAccess(memberId, bucketListId);
        bucketListRepository.delete(bucketList);
    }

    @Transactional
    public void reorderBucketLists(Long memberId, List<Long> bucketListIds) {
        Couple couple = coupleService.findCoupleByMemberId(memberId);

        // 현재 순서 기준으로 모든 버킷리스트 항목 조회
        List<BucketList> bucketLists = bucketListRepository.findByCoupleIdOrderByOrderNumAsc(couple.getId());

        // 버킷리스트 ID를 인덱스로 매핑 (빠른 조회를 위해)
        Map<Long, BucketList> bucketListMap = bucketLists.stream()
                .collect(Collectors.toMap(BucketList::getId, Function.identity()));

        // 새 순서 적용
        for (int i = 0; i < bucketListIds.size(); i++) {
            Long bucketId = bucketListIds.get(i);
            BucketList bucketList = bucketListMap.get(bucketId);

            if (bucketList != null && bucketList.getCouple().getId().equals(couple.getId())) {
                bucketList.updateOrder(i);
            }
        }
    }

    private BucketList findBucketListAndValidateAccess(Long memberId, Long bucketListId) {
        Couple couple = coupleService.findCoupleByMemberId(memberId);
        BucketList bucketList = bucketListRepository.findById(bucketListId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BUCKET_LIST_NOT_FOUND));

        if (!bucketList.getCouple().getId().equals(couple.getId())) {
            throw new BusinessException(ErrorCode.BUCKET_LIST_ACCESS_DENIED);
        }

        return bucketList;
    }
}
