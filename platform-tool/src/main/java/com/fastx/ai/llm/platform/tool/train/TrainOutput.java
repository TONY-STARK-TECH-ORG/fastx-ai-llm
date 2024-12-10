package com.fastx.ai.llm.platform.tool.train;

import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;

/**
 * @author stark
 */
public class TrainOutput implements IPlatformToolOutput {

    private String data;

    private String error;

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getData() {
        return data;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String getError() {
        return error;
    }
}
