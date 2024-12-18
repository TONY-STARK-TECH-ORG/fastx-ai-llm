package com.fastx.ai.llm.platform.tool.llm.function.imagine.types;

import org.springframework.util.Assert;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
public class OpenAIImagineRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String modelId;

    private String prompt;

    private String size;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public void validate() {
        Assert.notNull(prompt, "prompt can not be null");
        Assert.notNull(size, "size can not be null");
        Assert.notNull(modelId, "modelId can not be null");
        Assert.isTrue("256x256".equals(size) || "512x512".equals(size) || "1024x1024".equals(size), "size must be 256x256, 512x512 or 1024x1024");
    }
}
