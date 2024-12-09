package com.fastx.ai.llm.platform.tool.llm;

import com.fastx.ai.llm.platform.tool.spi.IPlatformToolInput;

/**
 * @author stark
 */
public class LLMInput implements IPlatformToolInput {

    private String data;

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getData() {
        return data;
    }
}
