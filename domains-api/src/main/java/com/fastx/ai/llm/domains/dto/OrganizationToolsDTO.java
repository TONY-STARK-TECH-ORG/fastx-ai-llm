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
public class OrganizationToolsDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long organizationId;

    private Long toolId;

    private String configData;

    public Long getToolId() {
        return toolId;
    }

    public void setToolId(Long toolId) {
        this.toolId = toolId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getConfigData() {
        return configData;
    }

    public void setConfigData(String configData) {
        this.configData = configData;
    }
}
