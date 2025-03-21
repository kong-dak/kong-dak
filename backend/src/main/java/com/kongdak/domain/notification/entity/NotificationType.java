package com.kongdak.domain.notification.entity;

import lombok.Getter;

@Getter
public enum NotificationType {
    // 커플 관련
    COUPLE_MATCH_REQUEST("커플 연결 요청이 왔습니다."),
    COUPLE_MATCH_ACCEPTED("커플 연결이 수락되었습니다."),
    COUPLE_MATCH_REJECTED("커플 연결이 거절되었습니다."),
    COUPLE_ANNIVERSARY("커플 기념일이 다가옵니다."),

    // 일정 관련
    SCHEDULE_CREATED("새로운 일정이 등록되었습니다."),
    SCHEDULE_UPDATED("일정이 수정되었습니다."),
    SCHEDULE_REMINDER("일정이 곧 시작됩니다."),

    // 일기 관련
    DIARY_CREATED("새로운 일기가 작성되었습니다."),
    DIARY_EMOJI("일기에 반응이 추가되었습니다."),

    // 데일리 질문
    DAILY_QUESTION_NEW("오늘의 질문이 도착했습니다."),
    DAILY_QUESTION_ANSWERED("상대방이 질문에 답변했습니다."),
    DAILY_QUESTION_REPLIED("답변에 댓글이 달렸습니다."),
    DAILY_QUESTION_EMOJI_CREATED("답변에 이모지가 달렸습니다."),
    // 버킷리스트
    BUCKET_CREATED("버킷리스트에 새 항목이 추가되었습니다."),
    BUCKET_COMPLETED("버킷리스트 항목이 완료되었습니다."),

    // 콕 찌르기
    POKE("상대방을 콕 찔렀습니다.");

    private final String defaultMessage;

    NotificationType(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
}

