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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author stark
 */
@AutoService(IPlatformTool.class)
public class PdfExtractToMarkdownProcessTool extends BasePreTrainTool {

    static Prototype _prototype = new Prototype();

    static {
        List<Fields> config = new ArrayList<>();
        // for inputs.
        List<Fields> inputs = new ArrayList<>();
        inputs.add(Fields.of("input", String.class, "https://oss.fastx-ai.com/fastx-ai-llm/8/2412.09618v1.pdf"));

        // for outputs.
        List<Fields> outputs = new ArrayList<>();
        outputs.add(Fields.of("markdown", List.class));

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

            String ext = Arrays.asList(pyInput.getInput().split("\\.")).getLast();
            String randomPath = UUID.randomUUID().toString();
            String randomName = randomPath + "." + ext;

            ProcessBuilder processBuilder = new ProcessBuilder("python", "platform-tool/src/main/resources/python-script/pdf2md.py", pyInput.getInput(), randomName, randomPath);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            List<String> results = readOutput(process.getInputStream());

            // result check!
            int exitCode = process.waitFor();
            Assert.isTrue(0 == exitCode, "Python process exited with code " + exitCode);

            String beginFlag = "---FAST-LLM-START---";
            String endFlag = "---FAST-LLM-END---";

            if (!(results.contains(beginFlag) && results.contains(endFlag))) {
                throw new ToolExecException("pdf file process not success!");
            }

            int start = results.indexOf(beginFlag);
            int end = results.indexOf(endFlag);

            String markdown = String.join("\n", results.subList(start, end));

            return TrainOutput.of(JSON.toJSONString(Map.of("markdown", markdown)), null);
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
        return "File Process";
    }

    @Override
    public String getDescription() {
        return "Process PDF to Markdown String (not support image inspect now, will support later).";
    }

    @Override
    public String getCode() {
        return "train.pre.file.pdf.extract";
    }

    @Override
    public String getPrototype() {
        return _prototype.toJSONString();
    }

    public static void main(String[] args) {
        TrainInput input = new TrainInput();
        input.setInputs(JSON.toJSONString(Map.of("input", "https://oss.fastx-ai.com/fastx-ai-llm/8/2412.09618v1.pdf")));
        PdfExtractToMarkdownProcessTool tool = new PdfExtractToMarkdownProcessTool();
        System.out.println(JSON.toJSONString(tool.exec(input)));
    }
}
