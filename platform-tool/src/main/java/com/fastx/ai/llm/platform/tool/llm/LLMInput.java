package com.fastx.ai.llm.platform.tool.llm;

import com.fastx.ai.llm.platform.tool.spi.IPlatformToolInput;

import java.io.OutputStream;

/**
 * @author stark
 */
public class LLMInput implements IPlatformToolInput {

    private String inputs;

    private String config;

    private OutputStream stream = null;

    @Override
    public OutputStream getStream() {
        return stream;
    }

    @Override
    public void setStream(OutputStream stream) {
        this.stream = stream;
    }


    @Override
    public void setInputs(String inputs) {
        this.inputs = inputs;
    }

    @Override
    public String getInputs() {
        return inputs;
    }

    @Override
    public String getConfig() {
        return config;
    }

    @Override
    public void setConfig(String config) {
        this.config = config;
    }

    public static LLMInput of(String data, String config) {
        LLMInput llmInput = new LLMInput();
        llmInput.setInputs(data);
        llmInput.setConfig(config);
        return llmInput;
    }
}
