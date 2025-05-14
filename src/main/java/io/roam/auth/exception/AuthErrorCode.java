package io.roam.auth.exception;

import org.springframework.http.HttpStatus;

import io.roam.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_4011", "잘못된 비밀번호"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_4041", "존재하지 않는 유저"),
    USER_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "AUTH_4091", "이미 존재하는 유저 아이디"),
    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "AUTH_4092", "이미 존재하는 유저 이메일"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_4011", "잘못된 비밀번호"),
    
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    
}
