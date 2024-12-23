package com.fastx.ai.llm.platform.tool.train.pre.embedding;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.platform.tool.entity.Fields;
import com.fastx.ai.llm.platform.tool.entity.Prototype;
import com.fastx.ai.llm.platform.tool.exception.ToolExecException;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.fastx.ai.llm.platform.tool.train.TrainInput;
import com.fastx.ai.llm.platform.tool.train.TrainOutput;
import com.fastx.ai.llm.platform.tool.train.pre.BasePreTrainTool;
import com.fastx.ai.llm.platform.tool.train.pre.embedding.input.EmbeddingInput;
import com.google.auto.service.AutoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author stark
 */
@AutoService(IPlatformTool.class)
public class TextBertEmbeddingProcessTool extends BasePreTrainTool {

    static Prototype _prototype = new Prototype();

    static {
        List<Fields> config = new ArrayList<>();
        // for inputs.
        List<Fields> inputs = new ArrayList<>();
        inputs.add(Fields.of("input", String.class, "hello world"));

        // for outputs.
        List<Fields> outputs = new ArrayList<>();
        outputs.add(Fields.of("embeddings", List.class));

        // add to
        _prototype.setConfig(config);
        _prototype.setInputs(inputs);
        _prototype.setOutputs(outputs);
    }

    @Override
    public TrainOutput exec(TrainInput input) {
        try {
            EmbeddingInput pyInput = JSON.parseObject(input.getInputs(), EmbeddingInput.class);
            Assert.isTrue(!StringUtils.isEmpty(pyInput.getInput()), "input can not be empty");

            ProcessBuilder processBuilder = new ProcessBuilder("python", "platform-tool/src/main/resources/python-script/text2vec.py", pyInput.getInput());
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            List<String> results = readOutput(process.getInputStream());

            if (CollectionUtils.isEmpty(results) || results.size() != 1) {
                throw new ToolExecException("text embedding process fail!");
            }

            String floatEmbeddingResult =  results.getFirst().trim();
            int exitCode = process.waitFor();
            Assert.isTrue(0 == exitCode, "Python process exited with code " + exitCode);

            String pattern = "\\[(.*)\\]";
            if (!Pattern.matches(pattern, floatEmbeddingResult)) {
                throw new ToolExecException("embedding result is illegal!");
            }

            List<Double> embeddingList = Arrays.asList(
                    floatEmbeddingResult.replace("[", "").replace("]", "").split(",")
            ).stream().map(Double::parseDouble).toList();

            return TrainOutput.of(JSON.toJSONString(Map.of("embeddings", embeddingList)), null);
        } catch (Exception e) {
            return TrainOutput.of(null, e.getMessage());
        }
    }

    private List<String> readOutput(InputStream inputStream) throws IOException {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines()
                    .collect(Collectors.toList());
        }
    }

    @Override
    public String getName() {
        return "Text Embedding (Bert)";
    }

    @Override
    public String getDescription() {
        return "Process TextInput like 'Hello world' to <Vec> use Bert tokenizer (only support plain text now, max(512) tokens!).";
    }

    @Override
    public String getCode() {
        return "train.pre.file.text.embedding";
    }

    @Override
    public String getPrototype() {
        return _prototype.toJSONString();
    }

    public static void main(String[] args) {
        TrainInput input = new TrainInput();
        input.setInputs(JSON.toJSONString(Map.of("input", "Hello World!")));
        TextBertEmbeddingProcessTool tool = new TextBertEmbeddingProcessTool();
        System.out.println(JSON.toJSONString(tool.exec(input)));
    }
}
