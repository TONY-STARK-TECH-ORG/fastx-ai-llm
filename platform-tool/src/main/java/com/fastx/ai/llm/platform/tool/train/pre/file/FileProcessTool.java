package com.fastx.ai.llm.platform.tool.train.pre.file;

import com.fastx.ai.llm.platform.tool.entity.Prototype;
import com.fastx.ai.llm.platform.tool.train.TrainInput;
import com.fastx.ai.llm.platform.tool.train.TrainOutput;
import com.fastx.ai.llm.platform.tool.train.pre.BasePreTrainTool;

/**
 * @author stark
 */
public class FileProcessTool extends BasePreTrainTool {

    static Prototype _prototype = new Prototype();

    @Override
    public TrainOutput exec(TrainInput input) {
        return null;
    }

    @Override
    public String getName() {
        return "File Process";
    }

    @Override
    public String getDescription() {
        return "Process file like ocr, embedding, split, etc.";
    }

    @Override
    public String getCode() {
        return "train.pre.file";
    }

    @Override
    public String getPrototype() {
        return _prototype.toJSONString();
    }
}
