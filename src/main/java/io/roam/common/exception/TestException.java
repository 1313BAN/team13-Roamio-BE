package io.roam.common.exception;

public class TestException extends DomainException {
    public TestException() {
        super(GlobalErrorCode.BAD_REQUEST);
    }
}
