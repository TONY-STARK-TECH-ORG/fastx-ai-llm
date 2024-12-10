package com.fastx.ai.llm.platform.tool.llm.model.openai.types;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author stark
 */
public class OpenAIRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String modelId;

    private List<OpenAIMessage> openAIMessages;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public List<OpenAIMessage> getMessages() {
        return openAIMessages;
    }

    public void setMessages(List<OpenAIMessage> openAIMessages) {
        this.openAIMessages = openAIMessages;
    }
}
