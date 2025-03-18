package com.kongdak.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Member 관련 예외
    MEMBER_NOT_FOUND(404, "M001", "회원을 찾을 수 없습니다"),
    DUPLICATE_EMAIL(400, "M002", "이미 존재하는 이메일입니다"),
    INVALID_NICKNAME(400, "M003", "올바르지 않은 닉네임 형식입니다"),
    INVALID_NICKNAME_LENGTH(400, "M004", "닉네임은 2자 이상 20자 이하여야 합니다"),
    INACTIVE_MEMBER(404, "M005", "비활성화된 회원입니다"),

    // OAUTH2.0 관련 예외
    INVALID_PROVIDER(400, "O001", "옳지 않은 PROVIDER 입니다."),
    INVALID_SOCIAL_TOKEN(400, "O002", "옳지 않은 소셜 토큰입니다."),
    EXPIRED_SOCIAL_TOKEN(400, "O003", "만료된 소셜 토큰입니다."),
    SOCIAL_SERVER_ERROR(400, "O004", "소셜 서버 에러가 났습니다.."),

    // Couple 관련 예외
    COUPLE_NOT_FOUND(404, "C001", "커플을 찾을 수 없습니다"),
    MEMBER_ALREADY_COUPLED(400, "C002", "이미 커플 관계가 있는 회원입니다"),
    PARTNER_ALREADY_COUPLED(400, "C003", "이미 커플 관계가 있는 파트너입니다"),
    NOT_COUPLE_MEMBER(403, "C004", "해당 커플의 구성원이 아닙니다"),
    CANNOT_RESTORE_COUPLE(400, "C005", "유예 기간이 지난 커플 관계는 복구할 수 없습니다"),
    COUPLE_MATCH_REQUEST_NOT_FOUND(404, "C006", "커플 요청을 찾을 수 없습니다."),
    INVALID_MATCH_REQUEST_CODE(400, "C007", "유효하지 않은 코드입니다."),
    CANNOT_MATCH_TO_OWN(400, "C008", "스스로에게 커플 요청을 할 수 없습니다."),
    COUPLE_ALREADY_EXISTS(400, "C009", "이미 커플이 맺어져 있습니다."),
    PARTNER_NOT_FOUND(404, "C010", "파트너를 찾을 수 없습니다."),
    COUPLE_ALREADY_DISCONNECTED(400, "C011", "이미 끊긴 커플입니다."),

    // Map 관련 예외
    PLACE_NOT_FOUND(404, "M001", "장소를 찾을 수 없습니다"),


    // Calendar 관련 예외
    CALENDAR_NOT_FOUND(404, "CL001", "캘린더를 찾을 수 없습니다"),
    SCHEDULE_NOT_FOUND(404, "CL002", "일정을 찾을 수 없습니다"),
    INVALID_SCHEDULE_TITLE(400, "CL003", "올바르지 않은 일정 제목입니다"),
    INVALID_SCHEDULE_TIME(400, "CL004", "일정 시작 시간이 종료 시간보다 빨라야 합니다"),
    SCHEDULE_ACCESS_DENIED(403, "CL005", "해당 일정에 대한 접근 권한이 없습니다"),
    CALENDAR_ACCESS_DENIED(403, "CL006", "해당 캘린더에 대한 접근 권한이 없습니다"),
    INVALID_SCHEDULE_PERIOD(400, "CL007", "올바르지 않은 일정 기간입니다"),
    INVALID_SCHEDULE_CATEGORY(400, "CL008", "올바르지 않은 일정 카테고리입니다"),
    DUPLICATE_SCHEDULE(400, "CL009", "해당 시간대에 이미 일정이 존재합니다"),
    DUPLICATE_CALENDAR(400, "CL010", "해당 커플의 캘린더가 이미 존재합니다"),
    MAX_SCHEDULE_TITLE_LENGTH(400, "CL011", "일정 제목은 30자를 초과할 수 없습니다"),

    // DailyQuestion 관련 예외
    QUESTION_NOT_FOUND(404, "Q001", "데일리 질문을 찾을 수 없습니다"),
    ANSWER_NOT_FOUND(404, "Q002", "답변을 찾을 수 없습니다"),
    ALREADY_ANSWERED(400, "Q003", "오늘의 질문에 이미 답변했습니다"),
    CANNOT_REACT_TO_OWN_ANSWER(400, "Q004", "자신의 답변에는 반응할 수 없습니다"),
    BOTH_ANSWERS_REQUIRED(400, "Q005", "댓글을 작성하기 전에 두 파트너 모두 답변해야 합니다"),
    REPLY_NOT_FOUND(404, "Q006", "댓글을 찾을 수 없습니다"),
    NOT_YOUR_REPLY(403, "Q007", "다른 사람의 댓글은 삭제할 수 없습니다"),
    NOT_YOUR_ANSWER(403, "Q008", "다른 사람의 답변은 삭제할 수 없습니다"),
    ANSWER_QUESTION_NOT_MATCH(404, "Q009", "질문과의 답변의 관계가 맞지 않습니다"),

    // Diary 관련 예외
    DIARY_NOT_FOUND(404, "D001", "다이어리를 찾을 수 없습니다."),
    DIARY_ALREADY_EXISTS(400, "D002", "선택한 날짜에 이미 다이어리가 존재합니다."),
    DIARY_BEING_EDITED(400, "D003", "다이어리가 다른 사람에 의해 수정 중입니다."),
    DIARY_ACCESS_DENIED(403, "D004", "다이어리 접근 권한이 없습니다."),

    // BucketList 관련 예외
    BUCKET_LIST_NOT_FOUND(404, "B001", "버킷리스트를 찾을 수 없습니다."),
    BUCKET_LIST_ACCESS_DENIED(403, "B002", "버킷리스트에 접근할 수 없습니다."),

    // Auth 관련 예외
    INVALID_REFRESH_TOKEN(401, "A001", "유효하지 않은 리프레시 토큰입니다"),
    INVALID_ACCESS_TOKEN(401, "A002", "유효하지 않은 액세스 토큰입니다"),
    EXPIRED_TOKEN(401, "A003", "만료된 토큰입니다"),
    UNAUTHORIZED_ACCESS(403, "A004", "인증되지 않은 접근입니다"),

    // SSE 관련 예외
    CANNOT_SEND_DUMMY_EVENT(400, "S001", "SSE DUMMY 이벤트 전송 실패"),
    CANNOT_CLOSE_EMITTER(400, "S002", "SSE Emitter를 닫을 수 없습니다"),

    // 파일 업로드 관련 예외
    FILE_UPLOAD_ERROR(400, "F001", "파일 업로드에 실패하였습니다."),
    FILE_DELETE_ERROR(400, "F001", "파일 삭제에 실패하였습니다."),

    // 시스템 예외
    INTERNAL_SERVER_ERROR(500, "S001", "내부 서버 오류가 발생했습니다");



    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}