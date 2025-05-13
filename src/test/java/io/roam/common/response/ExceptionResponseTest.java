package io.roam.common.response;

import io.roam.common.exception.ErrorCode;
import io.roam.common.exception.GlobalErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExceptionResponseTest {

    @Mock
    private ErrorCode mockErrorCode;

    @Test
    @DisplayName("ErrorResponse 생성 테스트 - GlobalErrorCode 사용")
    void createErrorResponseWithGlobalErrorCode() {
        // given
        ErrorCode errorCode = GlobalErrorCode.BAD_REQUEST;

        // when
        ExceptionResponse response = ExceptionResponse.of(errorCode);

        // then
        assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.success()).isFalse();
        assertThat(response.code()).isEqualTo("GLOBAL_400");
        assertThat(response.msg()).isEqualTo("Bad Request");
    }

    @Test
    @DisplayName("ErrorResponse 생성 테스트 - Mock ErrorCode 사용")
    void createErrorResponseWithMockErrorCode() {
        // given
        HttpStatus status = HttpStatus.FORBIDDEN;
        String code = "TEST_403";
        String message = "테스트 에러 메시지";
        
        when(mockErrorCode.getHttpStatus()).thenReturn(status);
        when(mockErrorCode.getCode()).thenReturn(code);
        when(mockErrorCode.getMessage()).thenReturn(message);

        // when
        ExceptionResponse response = ExceptionResponse.of(mockErrorCode);

        // then
        assertThat(response.status()).isEqualTo(status);
        assertThat(response.success()).isFalse();
        assertThat(response.code()).isEqualTo(code);
        assertThat(response.msg()).isEqualTo(message);
    }
    
    @Test
    @DisplayName("ErrorCode가 null일 경우 예외가 발생한다")
    void createErrorResponseWithNullErrorCode() {
        // given
        ErrorCode errorCode = null;

        // when & then
        assertThatThrownBy(() -> ExceptionResponse.of(errorCode))
                .isInstanceOf(NullPointerException.class);
    }
} 