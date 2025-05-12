package io.roam.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "GLOBAL_400", "Bad Request"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "GLOBAL_404", "존재하지 않는 API endpoint"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL_500", "예기치 못한 오류"),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN_401", "유효하지 않은 토큰"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN_401", "만료된 토큰"),
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
