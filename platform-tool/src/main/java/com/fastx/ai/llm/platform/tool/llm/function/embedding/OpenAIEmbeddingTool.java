package com.fastx.ai.llm.platform.tool.llm.function.embedding;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.platform.tool.entity.Fields;
import com.fastx.ai.llm.platform.tool.entity.Prototype;
import com.fastx.ai.llm.platform.tool.exception.ToolExecException;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.llm.LLMOutput;
import com.fastx.ai.llm.platform.tool.llm.function.BaseLlmFunction;
import com.fastx.ai.llm.platform.tool.llm.function.embedding.types.OpenAIEmbeddingRequest;
import com.fastx.ai.llm.platform.tool.llm.function.embedding.types.OpenAIEmbeddingResult;
import com.fastx.ai.llm.platform.tool.llm.function.embedding.types.OpenAIEmbeddingUsage;
import com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIConfig;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.CreateEmbeddingResponse;
import com.openai.models.EmbeddingCreateParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stark
 */
public class OpenAIEmbeddingTool extends BaseLlmFunction {

    public static Prototype _prototype = new Prototype();

    // call openAI Api for embedding.

    private static String API_BASE_URL = "baseUrl";
    private static String API_KEY = "apiKey";

    private static String MODEL_ID = "modelId";
    private static String INPUT = "input";

    static {
        // for config
        List<Fields> config = new ArrayList<>();
        config.add(Fields.of(API_KEY, String.class));
        config.add(Fields.of(API_BASE_URL, String.class, "https://api.openai.com/v1"));
        // for inputs.
        List<Fields> inputs = new ArrayList<>();
        inputs.add(Fields.of(MODEL_ID, String.class, "text-embedding-3-small"));
        inputs.add(Fields.of(INPUT, String.class, "hello world"));

        // for outputs.
        List<Fields> outputs = new ArrayList<>();
        outputs.add(Fields.of("model", String.class));
        outputs.add(Fields.of("embeddings", List.class));
        outputs.add(Fields.of("usage", OpenAIEmbeddingUsage.class));

        // add to
        _prototype.setConfig(config);
        _prototype.setInputs(inputs);
        _prototype.setOutputs(outputs);
    }

    @Override
    public LLMOutput exec(LLMInput input) {
        if (StringUtils.isAnyBlank(input.getConfig(), input.getInputs())) {
            throw new ToolExecException("openai need config and input data.");
        }

        // send request
        OpenAIConfig config = JSON.parseObject(input.getConfig(), OpenAIConfig.class);
        OpenAIEmbeddingRequest request = JSON.parseObject(input.getInputs(), OpenAIEmbeddingRequest.class);

        if (StringUtils.isAnyBlank(request.getModelId(), request.getInput())) {
            throw new ToolExecException("openai embedding request need modelId and input.");
        }

        try {
            OpenAIClient client = OpenAIOkHttpClient.builder()
                    .apiKey(config.getApiKey())
                    .baseUrl(config.getBaseUrl())
                    .build();
            // 1536 dimensions for ada v2 and suffix with `-small` embedding models.
            int dimensions = 1536;
            if (request.getModelId().endsWith("-large")) {
                dimensions = 3072;
            }
            EmbeddingCreateParams embeddingCreateParams = EmbeddingCreateParams.builder()
                    .model(request.getModelId())
                    .input(request.getInput())
                    // text dimensions fixed to 1536. large fixed to 3072.
                    .dimensions(dimensions)
                    .build();
            CreateEmbeddingResponse response = client.embeddings().create(embeddingCreateParams);
            if (CollectionUtils.isEmpty(response.data())) {
                throw new ToolExecException("openai embedding response data is empty.");
            }
            return LLMOutput.of(JSON.toJSONString(OpenAIEmbeddingResult.of(response)));
        } catch (Exception e) {
            return LLMOutput.ofError(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "OpenAI Embedding";
    }

    @Override
    public String getDescription() {
        return "OpenAI Embedding API";
    }

    @Override
    public String getCode() {
        return "llm.openai.embedding";
    }

    @Override
    public String getPrototype() {
        return _prototype.toJSONString();
    }
}
