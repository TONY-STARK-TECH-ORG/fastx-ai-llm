package com.fastx.ai.llm.platform.dto;

import java.io.Serializable;

/**
 * @author stark
 */
public class NodeDTO implements Serializable {

    private String name;
    /**
     * data contains: node input, node outputs
     */
    private String data;

    /**
     * based on tool version data
     */
    private ToolDTO tool;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ToolDTO getTool() {
        return tool;
    }

    public void setTool(ToolDTO tool) {
        this.tool = tool;
    }
}
