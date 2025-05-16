package io.roam.jwt.exception;

import io.roam.common.exception.DomainException;

public class ExpiredTokenException extends DomainException {
    public ExpiredTokenException() {
        super(JwtErrorCode.EXPIRED_TOKEN);
    }
} 