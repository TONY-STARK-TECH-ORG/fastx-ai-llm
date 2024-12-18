package com.fastx.ai.llm.platform.tool.llm.tool;

import com.fastx.ai.llm.platform.tool.llm.BaseLlmTool;

/**
 * @author stark
 */
public abstract class BaseLlmTools extends BaseLlmTool {

    @Override
    public String getType() {
        return "llm-tool";
    }

    @Override
    public String getIcon() {
        return "https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png";
    }

}
