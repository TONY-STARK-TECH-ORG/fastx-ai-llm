package com.fastx.ai.llm.platform.tool.llm;

import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;

/**
 * @author stark
 */
public class LLMOutput implements IPlatformToolOutput {

    private String data;

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getData() {
        return data;
    }
}
