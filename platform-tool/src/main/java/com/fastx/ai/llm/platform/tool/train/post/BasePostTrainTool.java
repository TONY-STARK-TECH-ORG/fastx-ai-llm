package com.fastx.ai.llm.platform.tool.train.post;

import com.fastx.ai.llm.platform.tool.train.BaseTrainTool;

/**
 * @author stark
 */
public abstract class BasePostTrainTool extends BaseTrainTool {

    @Override
    public String getType() {
        return "train-post";
    }
}
