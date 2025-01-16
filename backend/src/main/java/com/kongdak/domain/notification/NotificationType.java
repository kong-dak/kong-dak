package com.kongdak.domain.notification;

public enum NotificationType {
    COUPLE_MATCH_REQUEST("커플 연결 요청이 왔습니다."),
    COUPLE_MATCH_ACCEPTED("커플 연결이 수락되었습니다."),
    COUPLE_MATCH_REJECTED("커플 연결이 거절되었습니다.");

    private final String defaultMessage;

    NotificationType(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}

