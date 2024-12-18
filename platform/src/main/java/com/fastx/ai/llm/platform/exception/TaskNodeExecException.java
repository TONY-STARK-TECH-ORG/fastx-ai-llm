package com.fastx.ai.llm.platform.exception;

/**
 * @author stark
 */
public class TaskNodeExecException extends RuntimeException {

    public TaskNodeExecException() {
        super();
    }

    public TaskNodeExecException(String message) {
        super(message);
    }

    public TaskNodeExecException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskNodeExecException(Throwable cause) {
        super(cause);
    }

    protected TaskNodeExecException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
