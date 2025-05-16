package io.roam.jwt;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.roam.common.exception.DomainException;
import io.roam.common.type.GlobalErrorCode;
import io.roam.common.response.ExceptionResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        log.info("cause {}", authException.getCause());
        // 예외 유형에 따라 적절한 응답 생성
        ExceptionResponse<?> errorResponse;
        
        if (authException.getCause() instanceof DomainException domainException) {
            errorResponse = ExceptionResponse.of(domainException.getErrorCode());
        } else {
            errorResponse = ExceptionResponse.of(GlobalErrorCode.AUTHENTICATION_REQUIRED);
        }
        
        // JSON 형태로 응답 반환
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorResponse.getStatus().value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
} 