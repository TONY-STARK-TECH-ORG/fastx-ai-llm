package com.fastx.ai.llm.platform.tool.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;

import java.util.List;

/**
 * @author stark
 */
public class Prototype {

    private List<Fields> inputs;

    private List<Fields> outputs;

    private List<Fields> config;

    public String toJSONString() {
        JSONWriter.Feature[] futures = {JSONWriter.Feature.WriteNullListAsEmpty,
                JSONWriter.Feature.WriteNullStringAsEmpty,
                JSONWriter.Feature.WriteNullNumberAsZero,
                JSONWriter.Feature.WriteNullBooleanAsFalse};
        return JSON.toJSONString(this, futures);
    }

    public Fields getInputField(String name) {
        return inputs.stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
    }

    public Fields getConfigField(String name) {
        return config.stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
    }

    public List<Fields> getInputs() {
        return inputs;
    }

    public void setInputs(List<Fields> inputs) {
        this.inputs = inputs;
    }

    public List<Fields> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Fields> outputs) {
        this.outputs = outputs;
    }

    public List<Fields> getConfig() {
        return config;
    }

    public void setConfig(List<Fields> config) {
        this.config = config;
    }
}
