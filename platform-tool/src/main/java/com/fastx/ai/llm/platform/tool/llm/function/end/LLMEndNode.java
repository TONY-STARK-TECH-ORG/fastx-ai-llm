package com.fastx.ai.llm.platform.tool.llm.function.end;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.platform.tool.entity.Fields;
import com.fastx.ai.llm.platform.tool.entity.Prototype;
import com.fastx.ai.llm.platform.tool.exception.ToolExecException;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.llm.LLMOutput;
import com.fastx.ai.llm.platform.tool.llm.function.BaseLlmFunction;
import com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIMessage;
import com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIRequest;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.google.auto.service.AutoService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author stark
 */
@AutoService(IPlatformTool.class)
public class LLMEndNode extends BaseLlmFunction {

    public static Prototype _prototype = new Prototype();

    private static String MODEL_ID = "modelId";
    private static String MESSAGES = "messages";
    private static String CONTENT = "content";
    private static String USAGE = "usage";

    static {
        // build prototype to openAI
        List<Fields> config = new ArrayList<>();

        List<Fields> inputs = new ArrayList<>();
        inputs.add(Fields.of(MODEL_ID, String.class, "gpt-4o-mini"));
        inputs.add(Fields.ofArray(MESSAGES, OpenAIMessage.class));

        List<Fields> outputs = new ArrayList<>();
        outputs.add(Fields.of(MODEL_ID, String.class, "gpt-4o-mini"));
        outputs.add(Fields.of(CONTENT, String.class, ""));

        _prototype.setConfig(config);
        _prototype.setInputs(inputs);
        _prototype.setOutputs(outputs);
    }

    @Override
    public LLMOutput exec(LLMInput input) {
        if (StringUtils.isAnyBlank(input.getData())) {
            throw new ToolExecException("llm end node need input data.");
        }
        String content = "";
        String modelId = "";
        try {
            OpenAIRequest request = JSON.parseObject(input.getData(), OpenAIRequest.class);
            modelId = request.getModelId();
            Optional<OpenAIMessage> assistant =
                    request.getMessages().stream().filter(openAIMessage -> openAIMessage.getRole().equals("assistant")).findFirst();
            if (assistant.isPresent()) {
                content = assistant.get().getContent();
            }
        } catch (Exception e) {
            content = e.getMessage();
        }
        return LLMOutput.of(JSON.toJSONString(Map.of("content", content, "modelId", modelId)));
    }

    @Override
    public String getIcon() {
        return "https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png";
    }

    @Override
    public String getName() {
        return "LLM EndNode";
    }

    @Override
    public String getDescription() {
        return "LLM output node. assistant output, system output and more ...";
    }

    @Override
    public String getCode() {
        return "llm.end";
    }

    @Override
    public String getPrototype() {
        return _prototype.toJSONString();
    }
}
