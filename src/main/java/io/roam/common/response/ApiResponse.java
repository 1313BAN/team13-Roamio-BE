package io.roam.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.http.HttpStatus;

@Getter
@JsonPropertyOrder({"success", "data"})
public class ApiResponse<T> extends BaseResponse {
    private final T data;

    @Builder
    private ApiResponse(HttpStatus status, T data) {
        super(status, true);
        this.data = data;
    }

    /**
     * 성공 응답 (200 OK)
     *
     * @param <T> 페이로드 타입
     * @param data payload
     * @return HttpStatus.OK 상태와 페이로드를 포함한 SuccessResponse 인스턴스
     */
    public static <T> ApiResponse<T> of(@Nullable T data) {
        return new ApiResponse<>(HttpStatus.OK, data);
    }

    /**
     * 성공 응답 (HttpStatus 설정 필요)
     *
     * @param <T> 페이로드 타입
     * @param status HttpStatus Enum
     * @param data payload
     * @return 지정한 HttpStatus와 페이로드를 포함한 SuccessResponse 인스턴스
     */
    public static <T> ApiResponse<T> of(@NonNull HttpStatus status, @Nullable T data) {
        return new ApiResponse<>(status, data);
    }
}