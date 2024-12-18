package com.fastx.ai.llm.platform.tool.test;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.llm.LLMOutput;
import com.fastx.ai.llm.platform.tool.llm.tool.api.HttpSend;
import com.fastx.ai.llm.platform.tool.llm.tool.api.types.HttpRequest;

import java.io.IOException;

/**
 * @author stark
 */
public class HttpSendTest {

    public static void main(String[] args) throws IOException {
        HttpSend httpSend = new HttpSend();

        LLMInput input = new LLMInput();

        HttpRequest request = new HttpRequest();
        request.setUrl("http://www.baidu.com");
        request.setMethod("GET");

        input.setInputs(JSON.toJSONString(request));

        LLMOutput output = httpSend.exec(input);

        System.out.println(output.getOutputs());
        System.out.println(output.getError());
    }

}
