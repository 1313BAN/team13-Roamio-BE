package io.roam.auth.exception;

import io.roam.common.exception.DomainException;
import io.roam.auth.type.AuthErrorCode;

public class UserNotFoundException extends DomainException   {
    public UserNotFoundException() {
        super(AuthErrorCode.USER_NOT_FOUND);
    }
}
