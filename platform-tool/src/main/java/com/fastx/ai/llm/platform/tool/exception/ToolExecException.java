package com.fastx.ai.llm.platform.tool.exception;

/**
 * @author stark
 */
public class ToolExecException extends RuntimeException {

    public ToolExecException() {
    }

    public ToolExecException(String message) {
        super(message);
    }

    public ToolExecException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolExecException(Throwable cause) {
        super(cause);
    }

    public ToolExecException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
