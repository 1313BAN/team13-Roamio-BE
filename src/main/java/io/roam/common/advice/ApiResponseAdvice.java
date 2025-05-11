package io.roam.common.advice;

import io.roam.common.response.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import static org.springframework.web.servlet.function.ServerResponse.status;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<ApiResponse> {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return ApiResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public ApiResponse beforeBodyWrite(ApiResponse body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        response.setStatusCode(body.status());
        return body;
    }
}
