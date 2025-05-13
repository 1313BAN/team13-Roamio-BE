package io.roam.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400", "Bad Request"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_404", "존재하지 않는 API endpoint"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500", "예기치 못한 오류"),

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "COMMON_400", "입력값 검증에 실패했습니다"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
