package com.fastx.ai.llm.platform.tool.llm.tool.api;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.platform.tool.entity.Fields;
import com.fastx.ai.llm.platform.tool.entity.Prototype;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.llm.LLMOutput;
import com.fastx.ai.llm.platform.tool.llm.tool.BaseLlmTools;
import com.fastx.ai.llm.platform.tool.llm.tool.api.types.HttpRequest;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.google.auto.service.AutoService;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author stark
 */
@AutoService(IPlatformTool.class)
public class HttpSend extends BaseLlmTools {

    private static Prototype _prototype = new Prototype();

    static {
        List<Fields> config = new ArrayList<>();

        List<Fields> inputs = new ArrayList<>();
        inputs.add(Fields.of("url", String.class, "http://localhost:8080/api/v1/test"));
        inputs.add(Fields.of("method", String.class, "GET"));
        inputs.add(Fields.of("headers", List.class));
        inputs.add(Fields.of("body", String.class));
        inputs.add(Fields.of("timeout", Integer.class));
        inputs.add(Fields.of("mediaType", String.class));

        List<Fields> outputs = new ArrayList<>();
        outputs.add(Fields.of("response", Map.class));

        _prototype.setConfig(config);
        _prototype.setInputs(inputs);
        _prototype.setOutputs(outputs);
    }

    @Override
    public LLMOutput exec(LLMInput input) {
        try {
            Assert.isTrue(StringUtils.isNotEmpty(input.getInputs()), "input can't be empty.");
            HttpRequest reqInputs = JSON.parseObject(input.getInputs(), HttpRequest.class);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(reqInputs.getTimeout(), TimeUnit.MILLISECONDS)
                    .writeTimeout(reqInputs.getTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(reqInputs.getTimeout(), TimeUnit.MILLISECONDS)
                    .callTimeout(reqInputs.getTimeout(), TimeUnit.MILLISECONDS)
                    .build();

            Request request = null;
            Headers headers = Headers.of(reqInputs.getHeaders());

            if ("GET".equals(reqInputs.getMethod())) {
                request = new Request.Builder()
                        .url(reqInputs.getUrl())
                        .headers(headers)
                        .build();
            } else {
                MediaType mediaType = MediaType.get(reqInputs.getMediaType());
                RequestBody body = RequestBody.create(JSON.toJSONString(reqInputs.getBody()), mediaType);

                request = new Request.Builder()
                        .url(reqInputs.getUrl())
                        .headers(headers)
                        .post(body)
                        .build();
            }
            try (Response response = client.newCall(request).execute()) {
                return LLMOutput.of(response.body().string());
            }
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

    @Override
    public boolean needConfig() {
        return false;
    }
}
