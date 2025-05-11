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
    public static <T> SuccessResponse<T> of(@NonNull HttpStatus status, @Nullable T data) {
        return new SuccessResponse<>(status, true, data);
    }
}
