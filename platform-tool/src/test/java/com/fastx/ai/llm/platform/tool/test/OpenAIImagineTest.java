package com.fastx.ai.llm.platform.tool.test;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.llm.LLMOutput;
import com.fastx.ai.llm.platform.tool.llm.function.imagine.OpenAIImageGenerateTool;
import com.fastx.ai.llm.platform.tool.llm.function.imagine.types.OpenAIImagineRequest;
import com.fastx.ai.llm.platform.tool.llm.function.imagine.types.OpenAIImagineResponse;
import com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIConfig;

import java.io.IOException;

/**
 * @author stark
 */
public class OpenAIImagineTest {

    public static void main(String[] args) throws IOException {
        OpenAIImageGenerateTool openAIEmbeddingTool = new OpenAIImageGenerateTool();

        LLMInput input = new LLMInput();

        OpenAIConfig config = new OpenAIConfig();
        config.setApiKey("sk-");
        config.setBaseUrl("https://");
        config.setStreaming(false);

        OpenAIImagineRequest request = new OpenAIImagineRequest();
        request.setModelId("dall-e-3");
        request.setPrompt("Hello World!");
        request.setSize("1024x1024");

        input.setConfig(JSON.toJSONString(config));
        input.setInputs(JSON.toJSONString(request));

        LLMOutput output = openAIEmbeddingTool.exec(input);

        OpenAIImagineResponse result = JSON.parseObject(output.getOutputs(), OpenAIImagineResponse.class);
        System.out.println(result.getUrls());
    }

}
