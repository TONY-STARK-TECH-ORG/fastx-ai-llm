package com.fastx.ai.llm.platform.tool.spi;

import java.io.OutputStream;

/**
 * @author stark
 */
public class SimplePlatformToolInput implements IPlatformToolInput {

    private String inputs;
    private String config;
    private OutputStream stream;

    @Override
    public void setInputs(String inputs) {
        this.inputs = inputs;
    }

    @Override
    public void setConfig(String config) {
        this.config = config;
    }

    @Override
    public void setStream(OutputStream stream) {
        this.stream = stream;
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
    public OutputStream getStream() {
        return stream;
    }
}
