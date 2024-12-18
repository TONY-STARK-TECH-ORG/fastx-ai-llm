package com.fastx.ai.llm.platform.tool.test;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.llm.LLMOutput;
import com.fastx.ai.llm.platform.tool.llm.function.embedding.OpenAIEmbeddingTool;
import com.fastx.ai.llm.platform.tool.llm.function.embedding.types.OpenAIEmbeddingRequest;
import com.fastx.ai.llm.platform.tool.llm.function.embedding.types.OpenAIEmbeddingResult;
import com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIConfig;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * @author stark
 */
public class OpenAIEmbeddingTest {

    public static void main(String[] args) throws IOException {
        OpenAIEmbeddingTool openAIEmbeddingTool = new OpenAIEmbeddingTool();

        LLMInput input = new LLMInput();

        OpenAIConfig config = new OpenAIConfig();
        config.setApiKey("sk-");
        config.setBaseUrl("https://");
        config.setStreaming(false);

        OpenAIEmbeddingRequest request = new OpenAIEmbeddingRequest();
        request.setModelId("text-embedding-3-small");
        request.setInput("Hello World!");

        input.setConfig(JSON.toJSONString(config));
        input.setInputs(JSON.toJSONString(request));

        LLMOutput output = openAIEmbeddingTool.exec(input);

        OpenAIEmbeddingResult result = JSON.parseObject(output.getOutputs(), OpenAIEmbeddingResult.class);
        int size = result.getEmbeddings().get(0).getEmbeddings().size();
        Assert.isTrue(size == 1536, "embedding result was not correct!, size was: " + size);

        System.out.println(JSON.toJSONString(output));
    }

}
