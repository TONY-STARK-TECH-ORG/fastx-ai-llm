package com.fastx.ai.llm.platform.exception;

/**
 * @author stark
 */
public class TaskExecException extends RuntimeException {

    public TaskExecException() {
        super();
    }

    public TaskExecException(String message) {
        super(message);
    }

    public TaskExecException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskExecException(Throwable cause) {
        super(cause);
    }

    protected TaskExecException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
