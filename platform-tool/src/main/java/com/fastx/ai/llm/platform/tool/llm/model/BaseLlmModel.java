package com.fastx.ai.llm.platform.tool.llm.model;

import com.fastx.ai.llm.platform.tool.llm.BaseLlmTool;

/**
 * @author stark
 */
public abstract class BaseLlmModel extends BaseLlmTool {

    @Override
    public String getType() {
        return "llm-model";
    }

}
