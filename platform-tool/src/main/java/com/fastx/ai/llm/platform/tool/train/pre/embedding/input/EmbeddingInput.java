package com.fastx.ai.llm.platform.tool.train.pre.embedding.input;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
public class EmbeddingInput implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String input;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
