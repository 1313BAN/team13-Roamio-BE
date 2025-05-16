package io.roam.auth.exception;

import io.roam.common.exception.DomainException;
import io.roam.auth.type.AuthErrorCode;

public class InvalidPasswordException extends DomainException {
    public InvalidPasswordException() {
        super(AuthErrorCode.INVALID_PASSWORD);
    }
}
