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
public class OrgWorkflowDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String status;

    private Long organizationId;

    private OrgWorkflowVersionDTO activeVersion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public OrgWorkflowVersionDTO getActiveVersion() {
        return activeVersion;
    }

    public void setActiveVersion(OrgWorkflowVersionDTO activeVersion) {
        this.activeVersion = activeVersion;
    }
}
