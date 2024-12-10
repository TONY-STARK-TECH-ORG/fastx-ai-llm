package com.fastx.ai.llm.platform.tool.llm.model.openai.types;

import com.openai.models.ChatCompletionMessageToolCall;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
public class OpenAIToolCall implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String type;
    private OpenAIFunctionCall Function;

    public static OpenAIToolCall of(ChatCompletionMessageToolCall toolCall) {
        OpenAIToolCall call = new OpenAIToolCall();
        call.setId(toolCall.id());
        call.setType(toolCall.type().toString());
        // functions
        ChatCompletionMessageToolCall.Function function = toolCall.function();
        OpenAIFunctionCall functionCall = new OpenAIFunctionCall();
        functionCall.setName(function.name());
        functionCall.setArguments(function.arguments());
        call.setFunction(functionCall);
        return call;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OpenAIFunctionCall getFunction() {
        return Function;
    }

    public void setFunction(OpenAIFunctionCall function) {
        Function = function;
    }
}
