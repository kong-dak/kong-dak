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
