package io.roam.common.exception;

import io.roam.common.type.GlobalErrorCode;

public class TestException extends DomainException {
    public TestException() {
        super(GlobalErrorCode.BAD_REQUEST);
    }
}
