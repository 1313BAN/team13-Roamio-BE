package io.roam.common.advice;

import io.roam.common.exception.DomainException;
import io.roam.common.exception.GlobalErrorCode;
import io.roam.common.response.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(DomainException.class)
    public ErrorResponse domainException(DomainException e) {
        return ErrorResponse.of(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error)-> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        return ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST)
            .success(false)
            .code(GlobalErrorCode.VALIDATION_FAILED.getCode())
            .msg(fieldErrors)
            .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorResponse handleNotFound() {
        return ErrorResponse.of(GlobalErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException() {
        return ErrorResponse.of(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }
}
