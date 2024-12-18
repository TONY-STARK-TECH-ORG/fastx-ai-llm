package com.fastx.ai.llm.platform.tool.llm;

import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;

/**
 * @author stark
 */
public class LLMOutput implements IPlatformToolOutput {

    private String outputs;

    private String error;

    public void setOutputs(String outputs) {
        this.outputs = outputs;
    }

    @Override
    public String getOutputs() {
        return outputs;
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
        output.setOutputs(data);
        return output;
    }

    public static LLMOutput ofError(String error) {
        LLMOutput output = empty();
        output.setError(error);
        return output;
    }
}
