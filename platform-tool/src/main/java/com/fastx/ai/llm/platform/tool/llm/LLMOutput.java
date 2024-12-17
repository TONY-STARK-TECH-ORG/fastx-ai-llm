package com.fastx.ai.llm.platform.tool.llm;

import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;

/**
 * @author stark
 */
public class LLMOutput implements IPlatformToolOutput {

    private String data;

    private String error;

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public LLMOutput() {
    }

    public static LLMOutput empty() {
        return new LLMOutput();
    }

    public static LLMOutput of(String data) {
        LLMOutput output = empty();
        output.setData(data);
        return output;
    }

    public static LLMOutput ofError(String error) {
        LLMOutput output = empty();
        output.setError(error);
        return output;
    }
}
