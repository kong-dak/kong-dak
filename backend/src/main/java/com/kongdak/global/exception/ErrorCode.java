package com.kongdak.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Member 관련 예외
    MEMBER_NOT_FOUND(404, "M001", "Member not found"),
    DUPLICATE_EMAIL(400, "M002", "Email already exists"),
    INVALID_NICKNAME(400, "M003", "Invalid nickname format"),
    INVALID_NICKNAME_LENGTH(400, "M004", "Nickname must be between 2 and 20 characters"),

    // Couple 관련 예외
    COUPLE_NOT_FOUND(404, "C001", "Couple not found"),
    MEMBER_ALREADY_COUPLED(400, "C002", "Member is already in a couple relationship"),
    PARTNER_ALREADY_COUPLED(400, "C003", "Partner is already in a couple relationship"),
    NOT_COUPLE_MEMBER(403, "C004", "Not a member of this couple"),
    CANNOT_RESTORE_COUPLE(400, "C005", "Cannot restore couple relationship after grace period"),

    // Calendar 관련 예외
    CALENDAR_NOT_FOUND(404, "CL001", "Calendar not found"),
    SCHEDULE_NOT_FOUND(404, "CL002", "Schedule not found"),
    INVALID_SCHEDULE_TITLE(400, "CL003", "Invalid schedule title"),
    INVALID_SCHEDULE_TIME(400, "CL004", "Schedule start time must be before end time"),
    SCHEDULE_ACCESS_DENIED(403, "CL005", "No permission to access this schedule"),
    CALENDAR_ACCESS_DENIED(403, "CL006", "No permission to access this calendar"),
    INVALID_SCHEDULE_PERIOD(400, "CL007", "Invalid schedule period"),
    INVALID_SCHEDULE_CATEGORY(400, "CL008", "Invalid schedule category"),
    DUPLICATE_SCHEDULE(400, "CL009", "Schedule already exists for this time period"),
    DUPLICATE_CALENDAR(400, "CL010", "Calendar already exists for this couple"),
    MAX_SCHEDULE_TITLE_LENGTH(400, "CL011", "Schedule title must not exceed 30 characters"),

    // DailyQuestion 관련 예외
    QUESTION_NOT_FOUND(404, "Q001", "Daily question not found"),
    ANSWER_NOT_FOUND(404, "Q002", "Answer not found"),
    ALREADY_ANSWERED(400, "Q003", "Already answered today's question"),
    CANNOT_REACT_TO_OWN_ANSWER(400, "Q004", "Cannot react to your own answer"),
    BOTH_ANSWERS_REQUIRED(400, "Q005", "Both partners must answer before adding replies"),
    REPLY_NOT_FOUND(404, "Q006", "Reply not found"),
    NOT_YOUR_REPLY(403, "Q007", "Cannot delete other's reply"),

    // Auth 관련 예외
    INVALID_TOKEN(401, "A001", "Invalid token"),
    EXPIRED_TOKEN(401, "A002", "Token has expired"),
    UNAUTHORIZED_ACCESS(403, "A003", "Unauthorized access"),

    // 시스템 예외
    INTERNAL_SERVER_ERROR(500, "S001", "Internal server error");


    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
