package com.fastx.ai.llm.web.controller.error;

import com.fastx.ai.llm.web.controller.entity.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author stark
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Response<Void> handle(Exception exception) {
        return Response.error(exception.getMessage());
    }

}
