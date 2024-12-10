package com.fastx.ai.llm.platform.tool.train;

import com.fastx.ai.llm.platform.tool.spi.IPlatformToolInput;

import java.io.OutputStream;

/**
 * @author stark
 */
public class TrainInput implements IPlatformToolInput {

    private String data;

    private String config;

    @Override
    public String getConfig() {
        return config;
    }

    @Override
    public OutputStream getStream() {
        return null;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getData() {
        return data;
    }
}
