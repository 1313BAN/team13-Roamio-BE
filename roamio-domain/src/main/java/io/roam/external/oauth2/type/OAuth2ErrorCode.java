package io.roam.external.oauth2.type;

import io.roam.common.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OAuth2ErrorCode implements ErrorCode {
    AUTHENTICATION_FAILED(HttpStatus.BAD_REQUEST, "OAUTH2_401", "인증에 실패했습니다"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
