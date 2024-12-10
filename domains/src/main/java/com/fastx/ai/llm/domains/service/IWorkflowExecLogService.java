package com.fastx.ai.llm.domains.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastx.ai.llm.domains.entity.WorkflowExecLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IWorkflowExecLogService extends IService<WorkflowExecLog> {

    /**
     * get workflow exec log by version ids
     * @param versionIds version id list
     * @return all log list
     */
    boolean removeWorkflowExecLogByWorkflowVersionIds(List<Long> versionIds);

    /**
     * get workflow exec log by version id
     * @param workflowVersionId version id
     * @param page page
     * @param size size
     * @return WorkflowExecLog list with page info
     */
    Page<WorkflowExecLog> getWorkflowExecLogsByWorkflowVersionId(Long workflowVersionId, Long page, Long size);
}
