package com.fastx.ai.llm.platform.tool.llm.function.embedding.types;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
public class OpenAIEmbeddingRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String modelId;

    private String input;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
