package com.fastx.ai.llm.platform.tool.llm.tool.api;

import com.fastx.ai.llm.platform.tool.entity.Fields;
import com.fastx.ai.llm.platform.tool.entity.Prototype;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.llm.LLMOutput;
import com.fastx.ai.llm.platform.tool.llm.tool.BaseLlmTools;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stark
 */
public class HttpSend extends BaseLlmTools {

    static Prototype _prototype = new Prototype();

    static {
        List<Fields> config = new ArrayList<>();

        List<Fields> inputs = new ArrayList<>();
        inputs.add(Fields.of("url", String.class, "http://localhost:8080/api/v1/test"));
        inputs.add(Fields.of("method", String.class, "GET"));
        inputs.add(Fields.of("headers", List.class));
        inputs.add(Fields.of("body", String.class));
        inputs.add(Fields.of("timeout", Integer.class));

        List<Fields> outputs = new ArrayList<>();
        outputs.add(Fields.of("response", String.class));

        _prototype.setConfig(config);
        _prototype.setInputs(inputs);
        _prototype.setOutputs(outputs);
    }

    @Override
    public LLMOutput exec(LLMInput input) {
        try {
            return LLMOutput.of("");
        } catch (Exception e) {
            return LLMOutput.ofError(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Http API";
    }

    @Override
    public String getDescription() {
        return "Http API: For http call.";
    }

    @Override
    public String getCode() {
        return "llm.tool.http.send";
    }

    @Override
    public String getPrototype() {
        return _prototype.toJSONString();
    }
}
