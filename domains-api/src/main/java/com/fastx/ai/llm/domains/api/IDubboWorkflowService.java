package com.fastx.ai.llm.domains.api;

import com.fastx.ai.llm.domains.dto.PageDTO;
import com.fastx.ai.llm.domains.dto.WorkflowDTO;
import com.fastx.ai.llm.domains.dto.WorkflowExecLogDTO;
import com.fastx.ai.llm.domains.dto.WorkflowVersionDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IDubboWorkflowService {

    //-----------------------------------------------------------------------------------------
    // flow
    //-----------------------------------------------------------------------------------------

    /**
     * create workflow
     * @param workflowDTO workflow info
     * @return workflow obj
     */
    WorkflowDTO createWorkflow(WorkflowDTO workflowDTO);

    /**
     * update work flow
     * @param workflowDTO workflow info
     * @return true or false
     */
    boolean updateWorkflow(WorkflowDTO workflowDTO);

    /**
     * delete workflow, will delete all versions, version logs
     * @param workflowId id
     * @return true or false
     */
    boolean deleteWorkflow(Long workflowId);

    /**
     * get workflows by organization id
     * @param organizationId id
     * @return workflows
     */
    List<WorkflowDTO> getWorkflowsByOrganizationId(Long organizationId);

    //-----------------------------------------------------------------------------------------
    // version
    //-----------------------------------------------------------------------------------------

    /**
     * create workflow version
     * @param workflowVersionDTO version info
     * @return version obj
     */
    WorkflowVersionDTO createWorkflowVersion(WorkflowVersionDTO workflowVersionDTO);

    /**
     * update workflow version
     * @param workflowVersionDTO version info
     * @return true or false
     */
    boolean updateWorkflowVersion(WorkflowVersionDTO workflowVersionDTO);

    /**
     * delete workflow version, will delete all version logs.
     * @param workflowVersionId id
     * @return true or false
     */
    boolean deleteWorkflowVersion(Long workflowVersionId);

    /**
     * get workflow version
     * @param workflowVersionId id
     * @return version obj
     */
    WorkflowVersionDTO getWorkflowVersion(Long workflowVersionId);

    /**
     * get workflow versions by workflow id
     * @param workFlowId id
     * @return versions
     */
    List<WorkflowVersionDTO> getWorkflowVersionsByWorkflowId(Long workFlowId);

    //-----------------------------------------------------------------------------------------
    // exec log
    //-----------------------------------------------------------------------------------------

    /**
     * create workflow exec log info
     * @param workflowExecLogDTO exec log info
     * @return exec log obj
     */
    WorkflowExecLogDTO createWorkflowExecLog(WorkflowExecLogDTO workflowExecLogDTO);

    /**
     * get workflow exec log
     * @param workflowExecLogId id
     * @return exec log obj
     */
    WorkflowExecLogDTO getWorkflowExecLog(Long workflowExecLogId);

    /**
     * get workflow exec logs by workflow version id
     * @param workflowVersionId id
     * @param page page
     * @param size size
     * @return page
     */
    PageDTO<WorkflowExecLogDTO> getWorkflowExecLogsByWorkflowVersionId(Long workflowVersionId, Long page, Long size);
}
