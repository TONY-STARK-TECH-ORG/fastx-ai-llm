package com.fastx.ai.llm.platform.tool.llm;

import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;

/**
 * @author stark
 */
public abstract class BaseLlmTool implements IPlatformTool<LLMInput, LLMOutput> {

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getAuthor() {
        return "fastx-ai";
    }

    @Override
    public String getStatus() {
        return "active";
    }

    @Override
    public LLMOutput exec(LLMInput input) {
        throw new UnsupportedOperationException();
    }
}
