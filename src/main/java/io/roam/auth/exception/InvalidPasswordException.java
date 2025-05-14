package io.roam.auth.exception;

import io.roam.common.exception.DomainException;

public class InvalidPasswordException extends DomainException {
    public InvalidPasswordException() {
        super(AuthErrorCode.INVALID_PASSWORD);
    }
}
