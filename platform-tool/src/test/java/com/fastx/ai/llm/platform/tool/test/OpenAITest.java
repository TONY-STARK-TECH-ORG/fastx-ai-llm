package com.fastx.ai.llm.platform.tool.test;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.llm.model.openai.OpenAI;
import com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIConfig;
import com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIMessage;
import com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIRequest;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolInput;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;

/**
 * @author stark
 */
public class OpenAITest {

    public static void main(String[] args) throws IOException {
        OpenAI openai = new OpenAI();

        LLMInput input = new LLMInput();

        OpenAIConfig config = new OpenAIConfig();
        config.setApiKey("sk-");
        config.setBaseUrl("https://");
        config.setStreaming(false);

        OpenAIRequest request = new OpenAIRequest();
        request.setModelId("gpt-4o-mini");

        OpenAIMessage openAIMessage = new OpenAIMessage();
        openAIMessage.setRole("user");
        openAIMessage.setContent("hi");
        request.setMessages(List.of(openAIMessage));

        input.setConfig(JSON.toJSONString(config));
        input.setData(JSON.toJSONString(request));
        PipedOutputStream stream = new PipedOutputStream();
        input.setStream(stream);

        PipedInputStream inputStream = new PipedInputStream(stream);
        InputStreamReader reader = new InputStreamReader(inputStream);

        RunThread runThread = new RunThread(openai, input);
        runThread.start();

        try {
            int data;
            while ((data = reader.read()) != -1) {
                System.out.println((char) data);
            }
        } finally {
            try {
                reader.close();
                inputStream.close();
            } finally {
                // ignored
            }
        }
    }

    public static class RunThread extends Thread {

        private IPlatformTool iPlatformTool;

        private IPlatformToolInput iPlatformToolInput;

        public RunThread(IPlatformTool iPlatformTool, IPlatformToolInput iPlatformToolInput) {
            this.iPlatformTool = iPlatformTool;
            this.iPlatformToolInput = iPlatformToolInput;
        }

        @Override
        public void run() {
            IPlatformToolOutput exec = iPlatformTool.exec(iPlatformToolInput);
            System.out.println(JSON.toJSONString(exec));
        }
    }

}
