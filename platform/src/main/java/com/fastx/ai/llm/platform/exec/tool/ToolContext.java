package com.fastx.ai.llm.platform.exec.tool;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.platform.config.ToolsLoader;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;
import org.apache.dubbo.common.stream.StreamObserver;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Map;

/**
 * @author stark
 */
public class ToolContext {

    private String toolCode;
    private String toolVersion;
    private String type;
    private Map<String, Object> input;

    private InputStreamReader reader;
    private PipedInputStream inputStream;

    private IPlatformTool tool;

    // input and output

    private LLMInput llmInput;
    private IPlatformToolOutput llmOutput;

    public ToolContext(String toolCode, String toolVersion, String type, Map<String, Object> input) {
        this.toolCode = toolCode;
        this.toolVersion = toolVersion;
        this.type = type;
        this.input = input;
    }

    public static ToolContext of(Map<String, Object> params) throws IOException {
        String toolCode = (String) params.get("toolCode");
        String toolVersion = (String) params.get("toolVersion");
        String type = (String) params.get("type");
        Map<String, Object> input = (Map<String, Object>) params.get("input");
        Assert.isTrue("llm-model".equals(type), "only support llm-model stream exec");
        // set used tool
        ToolContext toolContext = new ToolContext(toolCode, toolVersion, type, input);
        toolContext.setTool(ToolsLoader.getTool(toolCode, toolVersion, type));

        LLMInput in = new LLMInput();
        in.setConfig(JSON.toJSONString(input.get("config")));
        in.setData(JSON.toJSONString(input.get("inputs")));
        // set stream
        PipedOutputStream stream = new PipedOutputStream();
        in.setStream(stream);
        // set reader and stream
        PipedInputStream inputStream = new PipedInputStream(stream);

        toolContext.setInputStream(inputStream);
        toolContext.setReader(new InputStreamReader(inputStream));
        // set tool input
        toolContext.setLlmInput(in);
        return toolContext;
    }

    public void readTo(StreamObserver<String> observer, Logger logger) {
        // send data to stream.
        try {
            int data;
            while ((data = reader.read()) != -1) {
                observer.onNext(String.valueOf((char) data));
            }
        } catch (IOException e) {
            logger.error("read stream error", e);
        }
    }

    public void clean() {
        try {
            reader.close();
            inputStream.close();
        } catch (IOException e) {
            // ignored
        } finally {
            // ignored
        }
        PlatformToolExecutor.EXEC_CONTEXT.remove();
    }

    public IPlatformToolOutput getLlmOutput() {
        return llmOutput;
    }

    public void setLlmOutput(IPlatformToolOutput llmOutput) {
        this.llmOutput = llmOutput;
    }

    public LLMInput getLlmInput() {
        return llmInput;
    }

    public void setLlmInput(LLMInput llmInput) {
        this.llmInput = llmInput;
    }

    public IPlatformTool getTool() {
        return tool;
    }

    public void setTool(IPlatformTool tool) {
        this.tool = tool;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getToolVersion() {
        return toolVersion;
    }

    public void setToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }

    public InputStreamReader getReader() {
        return reader;
    }

    public void setReader(InputStreamReader reader) {
        this.reader = reader;
    }

    public PipedInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(PipedInputStream inputStream) {
        this.inputStream = inputStream;
    }
}
