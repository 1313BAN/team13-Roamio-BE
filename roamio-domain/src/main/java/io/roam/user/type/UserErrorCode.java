package io.roam.user.type;

import org.springframework.http.HttpStatus;

import io.roam.common.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_4041", "User not found"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}