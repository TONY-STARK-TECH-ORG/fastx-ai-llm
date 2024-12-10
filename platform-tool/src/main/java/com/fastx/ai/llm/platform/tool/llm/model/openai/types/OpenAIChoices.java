package com.fastx.ai.llm.platform.tool.llm.model.openai.types;

import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionMessage;
import com.openai.models.ChatCompletionMessageToolCall;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author stark
 */
public class OpenAIChoices implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String finishReason = "stop";

    private Long index = 0L;

    private OpenAIMessage openAIMessage;

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public OpenAIMessage getMessage() {
        return openAIMessage;
    }

    public void setMessage(OpenAIMessage openAIMessage) {
        this.openAIMessage = openAIMessage;
    }

    public static List<OpenAIChoices> of(List<ChatCompletion.Choice> choices) {
        return choices.stream().map(OpenAIChoices::of).collect(Collectors.toList());
    }

    public static OpenAIChoices of(ChatCompletion.Choice choice) {
        OpenAIChoices result = new OpenAIChoices();
        result.setIndex(choice.index());
        result.setFinishReason(choice.finishReason().toString());
        ChatCompletionMessage message = choice.message();
        OpenAIMessage openAIMessage = new OpenAIMessage();
        openAIMessage.setContent(message.content().isPresent() ? message.content().get() : "");
        openAIMessage.setRole(message.role().toString());
        result.setMessage(openAIMessage);
        if (message.functionCall().isPresent()) {
            openAIMessage.setFunctionCall(OpenAIFunctionCall.of(message.functionCall().get()));
        }
        if (message.toolCalls().isPresent()) {
            List<ChatCompletionMessageToolCall> toolCalls = message.toolCalls().get();
            openAIMessage.setToolCalls(toolCalls.stream().map(OpenAIToolCall::of).collect(Collectors.toList()));
        }
        return result;
    }
}
