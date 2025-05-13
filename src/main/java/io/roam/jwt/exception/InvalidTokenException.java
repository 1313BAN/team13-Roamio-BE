package io.roam.jwt.exception;

import io.roam.common.exception.DomainException;
import io.roam.common.exception.GlobalErrorCode;

public class InvalidTokenException extends DomainException{
    public InvalidTokenException() {
        super(GlobalErrorCode.INVALID_TOKEN);
    }
}
