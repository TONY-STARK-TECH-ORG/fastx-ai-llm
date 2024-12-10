package com.fastx.ai.llm.platform.tool.llm.model.openai.types;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author stark
 */
public class OpenAIResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private long created;
    private List<OpenAIChoices> choices;
    private OpenAIUsage completionUsage;

    public OpenAIResponse(String id, long created, List<OpenAIChoices> choices, OpenAIUsage completionUsage) {
        this.id = id;
        this.created = created;
        this.choices = choices;
        this.completionUsage = completionUsage;
    }

    public static OpenAIResponse of(String id, long created, List<OpenAIChoices> choices, OpenAIUsage completionUsage) {
        return new OpenAIResponse(id, created, choices, completionUsage);
    }

    public List<OpenAIChoices> getChoices() {
        return choices;
    }

    public void setChoices(List<OpenAIChoices> choices) {
        this.choices = choices;
    }

    public OpenAIUsage getCompletionUsage() {
        return completionUsage;
    }

    public void setCompletionUsage(OpenAIUsage completionUsage) {
        this.completionUsage = completionUsage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
