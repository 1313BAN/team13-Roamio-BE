package io.roam.common.controller;

import io.roam.common.exception.ErrorCode;
import io.roam.common.exception.GlobalErrorCode;
import io.roam.common.exception.TestException;
import io.roam.common.response.ApiResponse;
import io.roam.common.response.ErrorResponse;
import io.roam.common.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HealthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private HealthController healthController;

    @Test
    @DisplayName("성공 응답을 확인한다")
    void successResponse() throws Exception {
        // given
        SuccessResponse<String> response = SuccessResponse.of(HttpStatus.OK, "ok!!");
        doReturn(response).when(healthController).ok();

        // when & then
        mockMvc.perform(get("/health/ok"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("ok!!"));
    }

    @Test
    @DisplayName("예외 발생 시 에러 응답을 확인한다")
    void errorResponse() throws Exception {
        // given
        doThrow(new TestException()).when(healthController).ok();

        // when & then
        mockMvc.perform(get("/health/ok"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(GlobalErrorCode.BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.msg").value(GlobalErrorCode.BAD_REQUEST.getMessage()));
    }
    
    @Test
    @DisplayName("ErrorCode의 HttpStatus가 null일 경우 서버 오류(500)가 발생한다")
    void nullStatusErrorCodeResponse() throws Exception {
        // given
        ErrorCode nullStatusErrorCode = new ErrorCode() {
            @Override
            public HttpStatus getHttpStatus() {
                return null;
            }

            @Override
            public String getCode() {
                return "TEST_NULL_STATUS";
            }

            @Override
            public String getMessage() {
                return "테스트 메시지";
            }
        };
        
        // controller가 OK가 아닌 ErrorResponse를 반환하도록 설정
        doReturn(ErrorResponse.of(nullStatusErrorCode)).when(healthController).ok();

        // when & then
        mockMvc.perform(get("/health/ok"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(GlobalErrorCode.INTERNAL_SERVER_ERROR.getCode()))
                .andExpect(jsonPath("$.msg").value(GlobalErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }
} 