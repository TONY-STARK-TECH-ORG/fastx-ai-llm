package com.fastx.ai.llm.platform.tool.train;

import com.fastx.ai.llm.platform.tool.spi.IPlatformToolInput;

import java.io.OutputStream;

/**
 * @author stark
 */
public class TrainInput implements IPlatformToolInput {

    private String inputs;

    private String config;

    @Override
    public String getConfig() {
        return config;
    }

    @Override
    public void setStream(OutputStream stream) {
        // ignored
    }

    @Override
    public OutputStream getStream() {
        return null;
    }

    @Override
    public void setConfig(String config) {
        this.config = config;
    }

    @Override
    public void setInputs(String inputs) {
        this.inputs = inputs;
    }

    @Override
    public String getInputs() {
        return inputs;
    }
}
