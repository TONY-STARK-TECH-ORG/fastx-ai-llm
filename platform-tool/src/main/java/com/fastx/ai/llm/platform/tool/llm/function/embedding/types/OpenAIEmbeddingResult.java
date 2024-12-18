package com.fastx.ai.llm.platform.tool.llm.function.embedding.types;

import com.openai.models.CreateEmbeddingResponse;

import java.util.List;

/**
 * @author stark
 */
public class OpenAIEmbeddingResult {

    private String model;

    private List<OpenAIEmbedding> embeddings;

    private OpenAIEmbeddingUsage usage;

    public static OpenAIEmbeddingResult of(CreateEmbeddingResponse response) {
        OpenAIEmbeddingResult result = new OpenAIEmbeddingResult();
        result.setModel(response.model());
        result.setEmbeddings(response.data().stream().map(OpenAIEmbedding::of).toList());
        result.setUsage(OpenAIEmbeddingUsage.of(response.usage()));
        return result;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<OpenAIEmbedding> getEmbeddings() {
        return embeddings;
    }

    public void setEmbeddings(List<OpenAIEmbedding> embeddings) {
        this.embeddings = embeddings;
    }

    public OpenAIEmbeddingUsage getUsage() {
        return usage;
    }

    public void setUsage(OpenAIEmbeddingUsage usage) {
        this.usage = usage;
    }
}
