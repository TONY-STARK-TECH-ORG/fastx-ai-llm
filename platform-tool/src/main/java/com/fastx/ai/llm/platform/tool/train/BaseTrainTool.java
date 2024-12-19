package com.fastx.ai.llm.platform.tool.train;

import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;

/**
 * @author stark
 */
public abstract class BaseTrainTool implements IPlatformTool<TrainInput, TrainOutput> {

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
    public String getIcon() {
        return "https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png";
    }

    @Override
    public TrainOutput exec(TrainInput input) {
        throw new UnsupportedOperationException();
    }
}
