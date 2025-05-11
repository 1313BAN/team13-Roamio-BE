package io.roam.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.roam.common.exception.ErrorCode;
import lombok.NonNull;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        @JsonIgnore
        HttpStatus status,
        boolean success,
        String code,
        String msg
) implements ApiResponse {
    /**
     * 실패 응답
     *
     * @param errorCode ErrorCode Enum
     */
    public static ErrorResponse of(@NonNull ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getHttpStatus(), false, errorCode.getCode(), errorCode.getMessage());
    }
}