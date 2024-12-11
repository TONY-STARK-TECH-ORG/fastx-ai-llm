package com.fastx.ai.llm.platform.tool.llm.model.openai;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONPath;
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
import org.springframework.beans.BeanUtils;

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
    private static Map<String, Map<String, JSONPath>> _jsonPathCache = new HashMap<>();

    private static String API_BASE_URL = "baseURL";
    private static String API_KEY = "apiKey";
    private static String STREAMING = "isStreaming";

    private static String MODEL_ID = "modelId";
    private static String MESSAGES = "messages";

    static {
        // build prototype to openAI
        List<Fields> config = new ArrayList<>();
        config.add(Fields.of(API_KEY, String.class));
        config.add(Fields.of(API_BASE_URL, String.class, "https://api.openai.com/v1/chat/completions"));
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

        // create cache
        _jsonPathCache.put(CONFIG, new HashMap<>());
        _jsonPathCache.put(INPUTS, new HashMap<>());
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

        try {
            Prototype _pr = new Prototype();
            BeanUtils.copyProperties(_prototype, _pr);
            // validated fields
            _pr.getConfig().forEach(c -> {
                c.setExecValue(parseFields(_jsonPathCache, CONFIG, input.getConfig(), c));
            });
            _pr.getInputs().forEach(i -> {
                i.setExecValue(parseFields(_jsonPathCache, INPUTS, input.getData(), i));
            });
            // send request
            OpenAIConfig config = JSON.parseObject(input.getConfig(), OpenAIConfig.class);
            OpenAIRequest request = JSON.parseObject(input.getData(), OpenAIRequest.class);

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
                } catch (Exception e) {
                    output.setError(e.getMessage());
                } finally {
                    writer.close();
                    input.getStream().close();
                }
                OpenAIChoices choices = new OpenAIChoices();
                OpenAIMessage message = new OpenAIMessage();
                message.setRole(StringUtils.join(role.get(), ","));
                message.setContent(returned.get().toString());
                choices.setMessage(message);
                output.setData(JSON.toJSONString(OpenAIResponse.of(id.get(), created.get(), List.of(choices), usage.get())));
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
            throw new ToolExecException(e.getMessage(), e);
        }
    }

}
