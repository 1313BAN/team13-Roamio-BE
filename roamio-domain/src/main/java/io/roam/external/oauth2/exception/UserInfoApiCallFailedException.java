package io.roam.external.oauth2.exception;

import io.roam.common.exception.DomainException;
import io.roam.external.oauth2.type.OAuth2ErrorCode;

public class UserInfoApiCallFailedException extends DomainException {
    public UserInfoApiCallFailedException() {
        super(OAuth2ErrorCode.USER_INFO_API_CALL_FAILED);
    }
} 