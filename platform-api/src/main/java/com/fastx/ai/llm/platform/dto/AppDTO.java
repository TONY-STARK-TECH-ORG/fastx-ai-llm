package com.fastx.ai.llm.platform.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author stark
 */
public class AppDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String type;

    private String description;

    private String iconUrl;

    private String status;

    private Long organizationId;

    // output usage

    private OrgDTO organization;

    private List<AppVersionDTO> applicationVersions;

    private AppVersionDTO activeVersion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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

    public OrgDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrgDTO organization) {
        this.organization = organization;
    }

    public List<AppVersionDTO> getApplicationVersions() {
        return applicationVersions;
    }

    public void setApplicationVersions(List<AppVersionDTO> applicationVersions) {
        this.applicationVersions = applicationVersions;
    }

    public AppVersionDTO getActiveVersion() {
        return activeVersion;
    }

    public void setActiveVersion(AppVersionDTO activeVersion) {
        this.activeVersion = activeVersion;
    }
}
