package com.kongdak.domain.bucketlist;

import com.kongdak.domain.BaseTimeEntity;
import com.kongdak.domain.couple.Couple;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bucket_list")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class BucketList extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couple_id")
    private Couple couple;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private BucketListCategory category;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @Column(name = "order_num", nullable = false)
    private int orderNum;

    @Builder
    public BucketList(Couple couple, String title, BucketListCategory category, boolean isCompleted, int orderNum) {
        this.couple = couple;
        this.title = title;
        this.category = category;
        this.isCompleted = isCompleted;
        this.orderNum = orderNum;
    }
    public void updateTitle(String title) {
        this.title = title;
    }

    public void toggleCompletion() {
        this.isCompleted = !this.isCompleted;
    }

    public void updateOrder(int orderNum) {
        this.orderNum = orderNum;
    }
}
