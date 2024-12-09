package com.fastx.ai.llm.platform.tool.train.tokenizer;

import com.fastx.ai.llm.platform.tool.train.BaseTrainTool;

/**
 * @author stark
 */
public abstract class BaseTokenTool extends BaseTrainTool {

    @Override
    public String getType() {
        return "train-token";
    }
}
