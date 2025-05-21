package io.roam.external.oauth2.exception;

import io.roam.common.exception.DomainException;
import io.roam.external.oauth2.type.OAuth2ErrorCode;

public class TokenResponseNullException extends DomainException {
    public TokenResponseNullException() {
        super(OAuth2ErrorCode.TOKEN_RESPONSE_NULL);
    }
} 