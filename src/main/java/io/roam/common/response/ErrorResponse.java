package io.roam.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.roam.common.exception.ErrorCode;
import lombok.Builder;
import lombok.NonNull;

import org.springframework.http.HttpStatus;

@Builder
public record ErrorResponse(
        @JsonIgnore
        HttpStatus status,
        boolean success,
        String code,
        Object msg
) implements ApiResponse<Void> {
    /**
     * 실패 응답
     *
     * @param errorCode ErrorCode Enum
     * @return ErrorResponse 인스턴스
     */
    public static ErrorResponse of(@NonNull ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getHttpStatus(), false, errorCode.getCode(), errorCode.getMessage());
    }
}