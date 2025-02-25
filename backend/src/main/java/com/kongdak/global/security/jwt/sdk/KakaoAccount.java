package com.kongdak.global.security.jwt.sdk;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccount(
        @JsonProperty("has_email") Boolean hasEmail,
        @JsonProperty("email_needs_agreement") Boolean emailNeedsAgreement,
        @JsonProperty("is_email_valid") Boolean isEmailValid,
        @JsonProperty("is_email_verified") Boolean isEmailVerified,
        String email
) {
}
