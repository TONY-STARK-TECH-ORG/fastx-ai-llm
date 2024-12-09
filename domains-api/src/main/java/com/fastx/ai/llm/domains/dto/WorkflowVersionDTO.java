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
public class WorkflowVersionDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long workflowId;

    private String version;

    private String status;

    private String versionData;

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

    public String getVersionData() {
        return versionData;
    }

    public void setVersionData(String versionData) {
        this.versionData = versionData;
    }
}
