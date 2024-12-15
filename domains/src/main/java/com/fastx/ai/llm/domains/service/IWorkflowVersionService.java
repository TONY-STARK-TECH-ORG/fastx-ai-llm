package com.fastx.ai.llm.domains.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fastx.ai.llm.domains.entity.WorkflowVersion;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IWorkflowVersionService extends IService<WorkflowVersion> {

    /**
     * get workflow version by workflow id
     * @param workflowId workflow id
     * @return List<WorkflowVersion>
     */
    List<WorkflowVersion> getWorkflowVersionByWorkflowId(Long workflowId);

    /**
     * set other version to inactive
     * @param workflowId flow id
     */
    boolean setOtherVersionToInactive(Long workflowId);
}
