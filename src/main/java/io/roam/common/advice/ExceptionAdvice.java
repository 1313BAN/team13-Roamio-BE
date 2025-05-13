package io.roam.common.advice;

import io.roam.common.exception.DomainException;
import io.roam.common.exception.GlobalErrorCode;
import io.roam.common.response.ExceptionResponse;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(DomainException.class)
    public ExceptionResponse domainException(DomainException e) {
        return ExceptionResponse.of(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error)-> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        return ExceptionResponse.builder()
            .status(HttpStatus.BAD_REQUEST)
            .success(false)
            .code(GlobalErrorCode.VALIDATION_FAILED.getCode())
            .msg(fieldErrors)
            .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ExceptionResponse handleNotFound() {
        return ExceptionResponse.of(GlobalErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleException() {
        return ExceptionResponse.of(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }
}
