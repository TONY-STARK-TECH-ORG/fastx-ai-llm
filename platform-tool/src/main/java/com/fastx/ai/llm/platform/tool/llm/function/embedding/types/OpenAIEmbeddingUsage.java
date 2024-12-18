package com.fastx.ai.llm.platform.tool.llm.function.embedding.types;

import com.openai.models.CreateEmbeddingResponse;

/**
 * @author stark
 */
public class OpenAIEmbeddingUsage {

    private Long promptTokens;
    private Long totalTokens;

    public static OpenAIEmbeddingUsage of(CreateEmbeddingResponse.Usage usage) {
        OpenAIEmbeddingUsage u = new OpenAIEmbeddingUsage();
        u.setPromptTokens(usage.promptTokens());
        u.setTotalTokens(usage.totalTokens());
        return u;
    }

    public Long getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(Long promptTokens) {
        this.promptTokens = promptTokens;
    }

    public Long getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(Long totalTokens) {
        this.totalTokens = totalTokens;
    }
}
