package com.fastx.ai.llm.platform.tool.llm.model.openai.types;

import org.springframework.util.Assert;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
public class OpenAIConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String apiKey;

    private String baseUrl;

    private boolean streaming = false;

    public void validate() {
        Assert.notNull(apiKey, "apiKey can not be null");
        Assert.notNull(baseUrl, "baseUrl can not be null");
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean isStreaming() {
        return streaming;
    }

    public void setStreaming(boolean streaming) {
        this.streaming = streaming;
    }
}
