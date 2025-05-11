package io.roam.common.advice;

import io.roam.common.exception.DomainException;
import io.roam.common.exception.GlobalErrorCode;
import io.roam.common.response.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(DomainException.class)
    public ErrorResponse domainException(DomainException e) {
        return ErrorResponse.of(e.getErrorCode());
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
