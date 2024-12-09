package com.fastx.ai.llm.platform.tool.train.finetune;

import com.fastx.ai.llm.platform.tool.train.BaseTrainTool;

/**
 * @author stark
 */
public abstract class BaseFineTuningTool extends BaseTrainTool {

    @Override
    public String getType() {
        return "train-finetuning";
    }
}
