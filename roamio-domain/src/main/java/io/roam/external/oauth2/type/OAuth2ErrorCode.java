package io.roam.external.oauth2.type;

import io.roam.common.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OAuth2ErrorCode implements ErrorCode {
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "OAUTH2_4010", "인증에 실패했습니다"),
    TOKEN_RESPONSE_NULL(HttpStatus.BAD_GATEWAY, "OAUTH2_5020", "토큰 응답이 없습니다."),
    USER_INFO_API_CALL_FAILED(HttpStatus.BAD_GATEWAY, "OAUTH2_5021", "유저 정보 API 호출에 실패했습니다."),
    USER_INFO_RESPONSE_NULL(HttpStatus.BAD_GATEWAY, "OAUTH2_5022", "유저 정보 응답이 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
