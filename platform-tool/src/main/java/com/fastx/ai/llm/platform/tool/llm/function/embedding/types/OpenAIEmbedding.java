package com.fastx.ai.llm.platform.tool.llm.function.embedding.types;

import com.openai.models.Embedding;

import java.util.List;

/**
 * @author stark
 */
public class OpenAIEmbedding {

    private Long index;
    private List<Double> embeddings;

    public static OpenAIEmbedding of(Embedding embedding) {
        OpenAIEmbedding e = new OpenAIEmbedding();
        e.setIndex(embedding.index());
        e.setEmbeddings(embedding.embedding());
        return e;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public List<Double> getEmbeddings() {
        return embeddings;
    }

    public void setEmbeddings(List<Double> embeddings) {
        this.embeddings = embeddings;
    }
}
