package io.roam.auth.exception;

import io.roam.common.exception.DomainException;
import io.roam.auth.type.AuthErrorCode;

public class UserIdAlreadyExistsException extends DomainException {
    public UserIdAlreadyExistsException() {
        super(AuthErrorCode.USER_ID_ALREADY_EXISTS);
    }
}
