package io.roam.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.roam.common.exception.ErrorCode;
import lombok.*;

import org.springframework.http.HttpStatus;

@Getter
@JsonPropertyOrder({"success", "code", "msg"})
public class ExceptionResponse<T> extends BaseResponse {
    private final String code;
    private final T msg;

    @Builder
    private ExceptionResponse(HttpStatus status, String code, T msg) {
        super(status, false);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 실패 응답
     *
     * @param errorCode ErrorCode Enum
     * @return ErrorResponse 인스턴스
     */
    public static ExceptionResponse<?> of(@NonNull ErrorCode errorCode) {
        HttpStatus status = errorCode.getHttpStatus();
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ExceptionResponse<>(status, errorCode.getCode(), errorCode.getMessage());
    }
}