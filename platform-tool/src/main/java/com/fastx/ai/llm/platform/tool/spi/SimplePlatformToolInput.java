package com.fastx.ai.llm.platform.tool.spi;

import java.io.OutputStream;

/**
 * @author stark
 */
public class SimplePlatformToolInput implements IPlatformToolInput {

    private String data;
    private String config;
    private OutputStream stream;

    public void setInputs(String inputs) {
        this.data = inputs;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public void setStream(OutputStream stream) {
        this.stream = stream;
    }

    @Override
    public String getInputs() {
        return data;
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
