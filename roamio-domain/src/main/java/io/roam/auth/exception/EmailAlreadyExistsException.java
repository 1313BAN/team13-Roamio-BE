package io.roam.auth.exception;

import io.roam.common.exception.DomainException;
import io.roam.auth.type.AuthErrorCode;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException() {
        super(AuthErrorCode.USER_EMAIL_ALREADY_EXISTS);
    }
}
