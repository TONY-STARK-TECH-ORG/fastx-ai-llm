package com.fastx.ai.llm.platform.tool.llm.model.openai.types;

import com.openai.models.CompletionUsage;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
public class OpenAIUsage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long completionTokens = 0L;

    private Long promptTokens = 0L;

    private Long totalTokens = 0L;

    public Long getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(Long completionTokens) {
        this.completionTokens = completionTokens;
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

    public static OpenAIUsage of(CompletionUsage usage) {
        OpenAIUsage result = new OpenAIUsage();
        if (usage == null) {
            return result;
        }
        result.setTotalTokens(usage.totalTokens());
        result.setPromptTokens(usage.promptTokens());
        result.setCompletionTokens(usage.completionTokens());
        return result;
    }
}
