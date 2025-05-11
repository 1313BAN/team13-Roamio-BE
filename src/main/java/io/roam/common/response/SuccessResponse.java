package io.roam.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import lombok.NonNull;

import org.springframework.http.HttpStatus;

public record SuccessResponse<T>(
        @JsonIgnore
        HttpStatus status,
        boolean success,
        @Nullable T data
) implements ApiResponse {
    /**
     * 성공 응답 (200 OK)
     *
     * @param data payload
     */
    public static <T> SuccessResponse<T> of(@Nullable T data) {
        return new SuccessResponse<>(HttpStatus.OK, true, data);
    }

    /**
     * 성공 응답 (HttpStatus 설정 필요)
     *
     * @param status HttpStatus Enum
     * @param data payload
     */
    public static <T> SuccessResponse<T> of(@NonNull HttpStatus status, @Nullable T data) {
        return new SuccessResponse<>(status, true, data);
    }
}
