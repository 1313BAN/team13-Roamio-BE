package io.roam.auth.exception;

import io.roam.common.exception.DomainException;

public class UserAlreadyExistsException extends DomainException {
    public UserAlreadyExistsException() {
        super(AuthErrorCode.USER_ALREADY_EXISTS);
    }
}
