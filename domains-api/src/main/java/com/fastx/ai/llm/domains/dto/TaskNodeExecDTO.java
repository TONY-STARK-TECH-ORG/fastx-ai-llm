package com.fastx.ai.llm.domains.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public class TaskNodeExecDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long taskExecId;

    private String nodeId;

    private String parentNodeIds;

    private String startTime;

    private String endTime;

    private String status;

    private String inputs;

    private String outputs;

    private String config;

    public Long getTaskExecId() {
        return taskExecId;
    }

    public void setTaskExecId(Long taskExecId) {
        this.taskExecId = taskExecId;
    }

    public String getParentNodeIds() {
        return parentNodeIds;
    }

    public void setParentNodeIds(String parentNodeIds) {
        this.parentNodeIds = parentNodeIds;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInputs() {
        return inputs;
    }

    public void setInputs(String inputs) {
        this.inputs = inputs;
    }

    public String getOutputs() {
        return outputs;
    }

    public void setOutputs(String outputs) {
        this.outputs = outputs;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
