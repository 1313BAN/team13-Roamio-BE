package io.roam.external.oauth2.exception;

import io.roam.common.exception.DomainException;
import io.roam.external.oauth2.type.OAuth2ErrorCode;

public class UserInfoResponseNullException extends DomainException {
    public UserInfoResponseNullException() {
        super(OAuth2ErrorCode.USER_INFO_RESPONSE_NULL);
    }
} 