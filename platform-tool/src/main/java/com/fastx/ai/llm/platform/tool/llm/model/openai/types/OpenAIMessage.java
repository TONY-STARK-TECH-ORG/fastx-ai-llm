package com.fastx.ai.llm.platform.tool.llm.model.openai.types;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author stark
 */
public class OpenAIMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String role;
    private String content;
    private List<OpenAIToolCall> toolCalls;
    private OpenAIFunctionCall functionCall;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<OpenAIToolCall> getToolCalls() {
        return toolCalls;
    }

    public void setToolCalls(List<OpenAIToolCall> toolCalls) {
        this.toolCalls = toolCalls;
    }

    public OpenAIFunctionCall getFunctionCall() {
        return functionCall;
    }

    public void setFunctionCall(OpenAIFunctionCall functionCall) {
        this.functionCall = functionCall;
    }
}
