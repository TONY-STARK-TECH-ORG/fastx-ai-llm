package com.fastx.ai.llm.platform.tool.train.pre.chunking.input;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
public class ChunkingInput implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String input;

    private Integer chunkSize = 300;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }
}
