package io.roam.jwt.exception;

import org.springframework.security.core.AuthenticationException;

import io.roam.common.exception.DomainException;

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(DomainException cause) {
        super(cause.getMessage(), cause);
    }
}
