package com.fastx.ai.llm.platform.tool.train.pre;

import com.fastx.ai.llm.platform.tool.train.BaseTrainTool;

/**
 * @author stark
 */
public abstract class BasePreTrainTool extends BaseTrainTool {

    @Override
    public String getType() {
        return "train-pre";
    }
}
