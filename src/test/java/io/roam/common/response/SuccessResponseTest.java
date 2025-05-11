package io.roam.common.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SuccessResponseTest {

    @Test
    @DisplayName("SuccessResponse 생성 테스트")
    void createSuccessResponse() {
        // given
        String data = "test data";
        HttpStatus status = HttpStatus.OK;

        // when
        SuccessResponse<String> response = SuccessResponse.of(status, data);

        // then
        assertThat(response.status()).isEqualTo(status);
        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(data);
    }

    @Test
    @DisplayName("SuccessResponse null 데이터로 생성 테스트")
    void createSuccessResponseWithNullData() {
        // given
        HttpStatus status = HttpStatus.OK;

        // when
        SuccessResponse<Object> response = SuccessResponse.of(status, null);

        // then
        assertThat(response.status()).isEqualTo(status);
        assertThat(response.success()).isTrue();
        assertThat(response.data()).isNull();
    }
    
    @Test
    @DisplayName("status가 null일 경우 예외가 발생한다")
    void createSuccessResponseWithNullStatus() {
        // given
        HttpStatus status = null;
        String data = "test data";

        // when & then
        assertThatThrownBy(() -> SuccessResponse.of(status, data))
                .isInstanceOf(NullPointerException.class);
    }
} 