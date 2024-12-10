package com.fastx.ai.llm.platform.tool.llm.model.openai.types;

import com.openai.models.ChatCompletionMessage;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
public class OpenAIFunctionCall implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String arguments;
    private String name;

    public static OpenAIFunctionCall of(ChatCompletionMessage.FunctionCall functionCall) {
        OpenAIFunctionCall call = new OpenAIFunctionCall();
        call.setArguments(functionCall.arguments());
        call.setArguments(functionCall.arguments());
        return call;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
