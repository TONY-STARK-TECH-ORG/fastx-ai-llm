package com.fastx.ai.llm.platform.dto;

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
public class AppVersionDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long applicationId;

    private String version;

    private String status;

    /**
     * config and work flow version combine to => version data
     */
    private String config;
    /**
     * dependency: work flow
     */
    private FlowVersionDTO workflowVersion;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
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

    public FlowVersionDTO getWorkflowVersion() {
        return workflowVersion;
    }

    public void setWorkflowVersion(FlowVersionDTO workflowVersion) {
        this.workflowVersion = workflowVersion;
    }
}
