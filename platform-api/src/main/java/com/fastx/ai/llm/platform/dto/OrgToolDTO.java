package com.fastx.ai.llm.platform.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
public class OrgToolDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long organizationId;

    private String toolCode;

    private String toolVersion;

    private String configData;

    private Boolean custom;

    private String customCode;

    // For output.

    private ToolDTO toolDTO;


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getToolVersion() {
        return toolVersion;
    }

    public void setToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
    }

    public String getConfigData() {
        return configData;
    }

    public void setConfigData(String configData) {
        this.configData = configData;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public ToolDTO getToolDTO() {
        return toolDTO;
    }

    public void setToolDTO(ToolDTO toolDTO) {
        this.toolDTO = toolDTO;
    }
}
