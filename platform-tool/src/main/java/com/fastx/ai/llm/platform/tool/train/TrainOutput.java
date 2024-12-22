package com.fastx.ai.llm.platform.tool.train;

import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;

/**
 * @author stark
 */
public class TrainOutput implements IPlatformToolOutput {

    private String outputs;

    private String error;

    public static TrainOutput of(String data, String error) {
        TrainOutput output = new TrainOutput();
        output.outputs = data;
        output.error = error;
        return output;
    }

    public void setOutputs(String outputs) {
        this.outputs = outputs;
    }

    @Override
    public String getOutputs() {
        return outputs;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String getError() {
        return error;
    }
}
