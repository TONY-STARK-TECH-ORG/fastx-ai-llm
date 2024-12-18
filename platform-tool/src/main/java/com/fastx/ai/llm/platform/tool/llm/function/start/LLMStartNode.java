package com.fastx.ai.llm.platform.tool.llm.function.start;

import com.fastx.ai.llm.platform.tool.entity.Fields;
import com.fastx.ai.llm.platform.tool.entity.Prototype;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.llm.LLMOutput;
import com.fastx.ai.llm.platform.tool.llm.function.BaseLlmFunction;
import com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIMessage;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.google.auto.service.AutoService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stark
 */
@AutoService(IPlatformTool.class)
public class LLMStartNode extends BaseLlmFunction {

    public static Prototype _prototype = new Prototype();

    private static String MODEL_ID = "modelId";
    private static String MESSAGES = "messages";

    static {
        // build prototype to openAI
        List<Fields> config = new ArrayList<>();

        List<Fields> inputs = new ArrayList<>();
        inputs.add(Fields.of(MODEL_ID, String.class, "gpt-4o-mini"));
        inputs.add(Fields.ofArray(MESSAGES, OpenAIMessage.class));

        List<Fields> outputs = new ArrayList<>();
        outputs.add(Fields.of(MODEL_ID, String.class, "gpt-4o-mini"));
        outputs.add(Fields.ofArray(MESSAGES, OpenAIMessage.class));

        _prototype.setConfig(config);
        _prototype.setInputs(inputs);
        _prototype.setOutputs(outputs);
    }

    @Override
    public LLMOutput exec(LLMInput input) {
        if (StringUtils.isAnyBlank(input.getInputs())) {
            return LLMOutput.ofError("llm start node need input data.");
        }
        return LLMOutput.of(input.getInputs());
    }

    @Override
    public String getName() {
        return "LLM StartNode";
    }

    @Override
    public String getDescription() {
        return "LLM input node. user input, system input and more ...";
    }

    @Override
    public String getCode() {
        return "llm.function.start";
    }

    @Override
    public String getPrototype() {
        return _prototype.toJSONString();
    }
}
