package com.fastx.ai.llm.platform.tool.llm.tool.imagine;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.platform.tool.entity.Fields;
import com.fastx.ai.llm.platform.tool.entity.Prototype;
import com.fastx.ai.llm.platform.tool.exception.ToolExecException;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.llm.LLMOutput;
import com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIConfig;
import com.fastx.ai.llm.platform.tool.llm.tool.BaseLlmTools;
import com.fastx.ai.llm.platform.tool.llm.tool.imagine.types.OpenAIImagineRequest;
import com.fastx.ai.llm.platform.tool.llm.tool.imagine.types.OpenAIImagineResponse;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.google.auto.service.AutoService;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.Image;
import com.openai.models.ImageGenerateParams;
import com.openai.models.ImagesResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stark
 */
@AutoService(IPlatformTool.class)
public class OpenAIImagineTool extends BaseLlmTools {

    public static Prototype _prototype = new Prototype();

    // call openAI Api for embedding.

    private static String API_BASE_URL = "baseUrl";
    private static String API_KEY = "apiKey";

    private static String MODEL_ID = "modelId";
    private static String PROMPT = "prompt";
    private static String SIZE = "size";

    static {
        // for config
        List<Fields> config = new ArrayList<>();
        config.add(Fields.of(API_KEY, String.class));
        config.add(Fields.of(API_BASE_URL, String.class, "https://api.openai.com/v1"));
        // for inputs.
        List<Fields> inputs = new ArrayList<>();
        inputs.add(Fields.of(MODEL_ID, String.class, "dall-e-3"));
        inputs.add(Fields.of(PROMPT, String.class, "generate a man play game in fly."));
        inputs.add(Fields.of(SIZE, String.class, "512x512"));

        // for outputs.
        List<Fields> outputs = new ArrayList<>();
        outputs.add(Fields.of("urls", List.class));

        // add to
        _prototype.setConfig(config);
        _prototype.setInputs(inputs);
        _prototype.setOutputs(outputs);
    }

    @Override
    public LLMOutput exec(LLMInput input) {
        try {
            if (StringUtils.isAnyBlank(input.getConfig(), input.getInputs())) {
                throw new ToolExecException("openai need config and input data.");
            }

            // send request
            OpenAIConfig config = JSON.parseObject(input.getConfig(), OpenAIConfig.class);
            OpenAIImagineRequest request = JSON.parseObject(input.getInputs(), OpenAIImagineRequest.class);

            config.validate();
            request.validate();

            OpenAIClient client = OpenAIOkHttpClient.builder()
                    .apiKey(config.getApiKey())
                    .baseUrl(config.getBaseUrl())
                    .build();
            ImageGenerateParams imageGenerateParams = ImageGenerateParams.builder()
                    .model(request.getModelId())
                    .size(ImageGenerateParams.Size.of(request.getSize()))
                    .prompt(request.getPrompt())
                    .quality(ImageGenerateParams.Quality.HD)
                    .build();
            ImagesResponse generate = client.images().generate(imageGenerateParams);
            if (CollectionUtils.isEmpty(generate.data())) {
                throw new ToolExecException("openai embedding response data is empty.");
            }
            List<Image> images = generate.data();
            List<String> urls = new ArrayList<>();
            images.forEach(i -> {
                if (i.url().isPresent()) {
                    urls.add(i.url().get());
                }
            });
            return LLMOutput.of(JSON.toJSONString(OpenAIImagineResponse.of(urls)));
        } catch (Exception e) {
            return LLMOutput.ofError(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "OpenAI Dall.E";
    }

    @Override
    public String getDescription() {
        return "OpenAI Image generations API";
    }

    @Override
    public String getCode() {
        return "llm.function.openai.imagine";
    }

    @Override
    public String getPrototype() {
        return _prototype.toJSONString();
    }
}
