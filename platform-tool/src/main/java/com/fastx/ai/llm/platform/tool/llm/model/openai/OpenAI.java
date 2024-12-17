package com.fastx.ai.llm.platform.tool.llm.model.openai;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fastx.ai.llm.platform.tool.entity.Fields;
import com.fastx.ai.llm.platform.tool.entity.Prototype;
import com.fastx.ai.llm.platform.tool.exception.ToolExecException;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.llm.LLMOutput;
import com.fastx.ai.llm.platform.tool.llm.model.BaseLlmModel;
import com.fastx.ai.llm.platform.tool.llm.model.openai.types.*;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.google.auto.service.AutoService;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.ObjectMappers;
import com.openai.core.http.StreamResponse;
import com.openai.models.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author stark
 */
@AutoService(IPlatformTool.class)
public class OpenAI extends BaseLlmModel {

    public static Prototype _prototype = new Prototype();

    private static String API_BASE_URL = "baseUrl";
    private static String API_KEY = "apiKey";
    private static String STREAMING = "streaming";

    private static String MODEL_ID = "modelId";
    private static String MESSAGES = "messages";

    static {
        // build prototype to openAI
        List<Fields> config = new ArrayList<>();
        config.add(Fields.of(API_KEY, String.class));
        config.add(Fields.of(API_BASE_URL, String.class, "https://api.openai.com/v1"));
        config.add(Fields.of(STREAMING, Boolean.class));

        List<Fields> inputs = new ArrayList<>();
        inputs.add(Fields.of(MODEL_ID, String.class, "gpt-4o-mini"));
        inputs.add(Fields.ofArray(MESSAGES, OpenAIMessage.class));

        List<Fields> outputs = new ArrayList<>();
        outputs.add(Fields.of("id", String.class));
        outputs.add(Fields.of("created", Long.class));
        outputs.add(Fields.ofArray("choices", ChatCompletion.Choice.class));
        outputs.add(Fields.of("usage", CompletionUsage.class));

        _prototype.setConfig(config);
        _prototype.setInputs(inputs);
        _prototype.setOutputs(outputs);
    }

    @Override
    public String getIcon() {
        return "https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png";
    }

    @Override
    public String getName() {
        return "OpenAI";
    }

    @Override
    public String getDescription() {
        return "OpenAI Chat工具，支持流、非流调用全部会话模型接口。";
    }

    @Override
    public String getCode() {
        return "openai.chat";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getPrototype() {
        return _prototype.toJSONString();
    }

    @Override
    public LLMOutput exec(LLMInput input) {
        if (StringUtils.isAnyBlank(input.getConfig(), input.getData())) {
            throw new ToolExecException("openai need config and input data.");
        }

        // send request
        OpenAIConfig config = JSON.parseObject(input.getConfig(), OpenAIConfig.class);
        OpenAIRequest request = JSON.parseObject(input.getData(), OpenAIRequest.class);

        try {
            OpenAIClient client = OpenAIOkHttpClient.builder()
                    .apiKey(config.getApiKey())
                    .baseUrl(config.getBaseUrl())
                    .build();
            // create a request param
            ChatCompletionCreateParams completionCreateParams = ChatCompletionCreateParams.builder()
                    .model(ChatModel.of(request.getModelId()))
                    .messages(request.getMessages().stream().map(openAIMessage ->
                            ChatCompletionMessageParam.ofChatCompletionUserMessageParam(
                                    ChatCompletionUserMessageParam.builder()
                                            .role(ChatCompletionUserMessageParam.Role.of(openAIMessage.getRole()))
                                            .content(ChatCompletionUserMessageParam.Content.ofTextContent(openAIMessage.getContent()))
                                            .build()
                            )).collect(Collectors.toList()))
                    .streamOptions(ChatCompletionStreamOptions.builder().includeUsage(true).build())
                    .build();
            if (config.isStreaming()) {
                LLMOutput output = new LLMOutput();
                OutputStreamWriter writer = new OutputStreamWriter(input.getStream(), "UTF-8");
                // send streaming to output
                AtomicReference<String> id = new AtomicReference<>("");
                AtomicReference<Long> created = new AtomicReference<>(0L);
                AtomicReference<OpenAIUsage> usage = new AtomicReference<>(null);
                AtomicReference<StringBuilder> returned = new AtomicReference<>(new StringBuilder());
                AtomicReference<Set<String>> role = new AtomicReference<>(new HashSet<>());
                // create streaming
                try (StreamResponse<ChatCompletionChunk> messageStreamResponse = client.chat().completions().createStreaming(completionCreateParams)) {
                    messageStreamResponse.stream()
                            .map(m -> {
                                id.set(m.id());
                                created.set(m.created());
                                Optional<CompletionUsage> u = m.usage();
                                if (u.isPresent()) {
                                    usage.set(OpenAIUsage.of(u.get()));
                                }
                                return m;
                            })
                            .flatMap(completion -> completion.choices().stream())
                            .map(choice -> {
                                Optional<ChatCompletionChunk.Choice.Delta.Role> r = choice.delta().role();
                                if (r.isPresent()) {
                                    role.get().add(r.get().toString());
                                }
                                return choice;
                            })
                            .flatMap(choice -> choice.delta().content().stream())
                            .forEach(str -> {
                                try {
                                    returned.get().append(str);
                                    writer.write(str);
                                    writer.flush();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                    // complete stream response.
                    writer.write("<FASTX-EOF>");
                } catch (Exception e) {
                    writer.write(e.getMessage());
                    output.setError(e.getMessage());
                } finally {
                    // build response.
                    OpenAIChoices choices = new OpenAIChoices();
                    OpenAIMessage message = new OpenAIMessage();
                    message.setRole(StringUtils.join(role.get(), ","));
                    message.setContent(returned.get().toString());
                    choices.setMessage(message);
                    // to json result.
                    String jsonString = JSON.toJSONString(OpenAIResponse.of(id.get(), created.get(), List.of(choices), usage.get()));
                    output.setData(jsonString);
                    writer.write(jsonString);

                    // close writer and stream.
                    writer.close();
                    input.getStream().close();
                }
                return output;
            } else {
                // send full response to output
                ChatCompletion chatCompletion = client.chat().completions().create(completionCreateParams);
                String id = chatCompletion.id();
                long created = chatCompletion.created();
                List<ChatCompletion.Choice> choices = chatCompletion.choices();
                CompletionUsage usage = chatCompletion.usage().isPresent() ? chatCompletion.usage().get() : null;
                JsonMapper jsonMapper = ObjectMappers.jsonMapper();
                return LLMOutput.of(
                        jsonMapper.writeValueAsString(chatCompletion)
                );
            }
        } catch (Exception e) {
            if (config.isStreaming()) {
                try {
                    OutputStreamWriter writer = new OutputStreamWriter(input.getStream(), "UTF-8");
                    writer.write(e.getMessage());
                    writer.close();
                    input.getStream().close();
                } catch (Exception x) {
                    // ignored
                }

            }
            return LLMOutput.ofError(e.getMessage());
        }
    }

}
