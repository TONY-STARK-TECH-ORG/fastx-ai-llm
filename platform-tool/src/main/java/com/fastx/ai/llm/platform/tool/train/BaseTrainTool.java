package com.fastx.ai.llm.platform.tool.train;

import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;

/**
 * @author stark
 */
public abstract class BaseTrainTool implements IPlatformTool<TraiinInput, TrainOutput> {

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
    public TrainOutput exec(TraiinInput input) {
        throw new UnsupportedOperationException();
    }
}
