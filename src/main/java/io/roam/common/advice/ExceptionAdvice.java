package io.roam.common.advice;

import io.roam.common.exception.DomainException;
import io.roam.common.exception.GlobalErrorCode;
import io.roam.common.response.ExceptionResponse;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(DomainException.class)
    public ExceptionResponse<?> domainException(DomainException e) {
        return ExceptionResponse.of(e.getErrorCode());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ExceptionResponse<?> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ExceptionResponse.of(GlobalErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return ExceptionResponse.builder()
            .status(HttpStatus.BAD_REQUEST)
            .code(GlobalErrorCode.VALIDATION_FAILED.getCode())
            .msg(fieldErrors)
            .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ExceptionResponse<?> handleNotFound() {
        return ExceptionResponse.of(GlobalErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ExceptionResponse<?> handleException(Exception e) {
        log.error("내부 서버 오류 발생: ", e);
        return ExceptionResponse.of(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }
}
