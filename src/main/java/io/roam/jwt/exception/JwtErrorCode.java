package io.roam.jwt.exception;

import org.springframework.http.HttpStatus;

import io.roam.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "JWT_401", "유효하지 않은 토큰"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "JWT_401", "만료된 토큰"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "JWT_403", "접근 권한이 없습니다"),
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "JWT_401", "인증이 필요합니다"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
