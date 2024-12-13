package com.fastx.ai.llm.web.controller.entity;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
public class WorkspaceFileUploadRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long workspaceId;

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }
}
