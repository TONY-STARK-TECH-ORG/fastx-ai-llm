package com.fastx.ai.llm.domains.exception;

/**
 * @author stark
 */
public class DomainServiceLockException extends RuntimeException {

    public DomainServiceLockException() {
        super();
    }

    public DomainServiceLockException(String message) {
        super(message);
    }

    public DomainServiceLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainServiceLockException(Throwable cause) {
        super(cause);
    }

    protected DomainServiceLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
