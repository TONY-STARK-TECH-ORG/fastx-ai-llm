package com.fastx.ai.llm.platform.tool.llm.function;

import com.fastx.ai.llm.platform.tool.llm.BaseLlmTool;

/**
 *
 * llm-functions means: function for process input and output data.
 * like knowledge base embedding, ocr and more ...
 * like input dataset pre-process and more ...
 *
 * @author stark
 */
public abstract class BaseLlmFunction extends BaseLlmTool {

    @Override
    public String getType() {
        return "llm-function";
    }

    @Override
    public String getIcon() {
        return "https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png";
    }

}
