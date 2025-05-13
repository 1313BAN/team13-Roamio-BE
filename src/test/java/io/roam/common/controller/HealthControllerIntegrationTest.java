package io.roam.common.controller;

import io.roam.RoamioApplication;
import io.roam.common.exception.DomainException;
import io.roam.common.exception.ErrorCode;
import io.roam.common.exception.GlobalErrorCode;
import io.roam.common.exception.TestException;
import io.roam.common.response.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RoamioApplication.class)
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
        ApiResponse<String> response = ApiResponse.of(HttpStatus.OK, "ok!!");
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
        
        // controller가 예외를 던지도록 설정
        doThrow(new DomainException(nullStatusErrorCode)).when(healthController).ok();

        // when & then
        mockMvc.perform(get("/health/ok"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(nullStatusErrorCode.getCode()))
                .andExpect(jsonPath("$.msg").value(nullStatusErrorCode.getMessage()));
    }
} 