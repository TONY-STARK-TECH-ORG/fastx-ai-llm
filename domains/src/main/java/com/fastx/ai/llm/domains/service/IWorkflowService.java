package com.fastx.ai.llm.domains.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fastx.ai.llm.domains.entity.Workflow;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IWorkflowService extends IService<Workflow> {

    /**
     * get workflow list under org id
     * @param organizationId org id
     * @return workflow list
     */
    List<Workflow> getWorkflowsByOrganizationId(Long organizationId);
}
