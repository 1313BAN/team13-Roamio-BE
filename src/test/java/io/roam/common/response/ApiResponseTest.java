package io.roam.common.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApiResponseTest {

    @Test
    @DisplayName("SuccessResponse 생성 테스트")
    void createSuccessResponse() {
        // given
        String data = "test data";
        HttpStatus status = HttpStatus.OK;

        // when
        ApiResponse<String> response = ApiResponse.of(status, data);

        // then
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(data);
    }

    @Test
    @DisplayName("SuccessResponse null 데이터로 생성 테스트")
    void createSuccessResponseWithNullData() {
        // given
        HttpStatus status = HttpStatus.OK;

        // when
        ApiResponse<Object> response = ApiResponse.of(status, null);

        // then
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isNull();
    }
    
    @Test
    @DisplayName("status가 null일 경우 예외가 발생한다")
    void createSuccessResponseWithNullStatus() {
        // given
        HttpStatus status = null;
        String data = "test data";

        // when & then
        assertThatThrownBy(() -> ApiResponse.of(status, data))
                .isInstanceOf(NullPointerException.class);
    }
} 