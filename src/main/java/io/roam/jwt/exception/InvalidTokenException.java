package io.roam.jwt.exception;

import io.roam.common.exception.DomainException;

public class InvalidTokenException extends DomainException {

    public InvalidTokenException() {
        super(JwtErrorCode.INVALID_TOKEN);
    }
}
