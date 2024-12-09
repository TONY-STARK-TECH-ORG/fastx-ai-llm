package com.fastx.ai.llm.platform.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public class FlowVersionDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long workflowId;

    private String version;

    private String status;

    private String nodesData;

    private List<NodeDTO> nodes;

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNodesData() {
        return nodesData;
    }

    public void setNodesData(String nodesData) {
        this.nodesData = nodesData;
    }

    public List<NodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeDTO> nodes) {
        this.nodes = nodes;
    }
}
