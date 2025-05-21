package io.roam.external.oauth2.exception;

import io.roam.common.exception.DomainException;
import io.roam.external.oauth2.type.OAuth2ErrorCode;

public class AuthenticationFailedException extends DomainException {
    public AuthenticationFailedException() {
        super(OAuth2ErrorCode.AUTHENTICATION_FAILED);
    }
} 