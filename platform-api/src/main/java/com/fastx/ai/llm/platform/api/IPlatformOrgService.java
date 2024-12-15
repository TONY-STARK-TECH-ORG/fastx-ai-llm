package com.fastx.ai.llm.platform.api;

import com.fastx.ai.llm.platform.dto.*;

import java.util.List;

/**
 * @author stark
 */
public interface IPlatformOrgService {

    /**
     * get org by orgId
     * @param orgId
     * @return single org
     */
    OrgDTO getOrgById(Long orgId);

    /**
     * get org list by user id
     * @param userId
     * @return list
     */
    List<OrgDTO> getOrgByUserId(Long userId);

    //------------------------------------------------------------------------
    // tools
    //------------------------------------------------------------------------

    /**
     * create org tool
     * @param orgToolDTO info
     * @return create result
     */
    OrgToolDTO createOrgTools(OrgToolDTO orgToolDTO);

    /**
     * update org tool
     * @param orgToolsDTO with id update
     * @return true or false
     */
    boolean updateOrgTools(OrgToolDTO orgToolsDTO);

    /**
     * delete a tool org tool config
     * @param id id
     * @return result
     */
    boolean deleteOrgTools(Long id);

    /**
     * list all tools under orgs
     * @param orgId org id
     * @return tool list
     */
    List<OrgToolDTO> getOrgTools(Long orgId);

    //------------------------------------------------------------------------
    // task
    //------------------------------------------------------------------------

    /**
     * create a new task
     * @param taskDTO task info
     * @return created task
     */
    OrgTaskDTO createTask(OrgTaskDTO taskDTO);

    /**
     * update a task
     * @param taskDTO task info
     * @return true or false
     */
    boolean updateTask(OrgTaskDTO taskDTO);

    /**
     * delete a task, will delete all task log.
     * @param taskId task info
     * @return true or false
     */
    boolean deleteTask(Long taskId);

    /**
     * get all task under organization
     * @param organizationId organization id
     * @return task list
     */
    List<OrgTaskDTO> getTasksByOrgId(Long organizationId);

    /**
     * create new task log
     * @param taskLogDTO log info
     * @return created task log
     */
    OrgTaskLogDTO createTaskLog(OrgTaskLogDTO taskLogDTO);

    /**
     * all task logs
     * @param taskId task id
     * @param page page
     * @param size size
     * @param status status
     * @return task list with page info
     */
    PlatformPagaDTO<OrgTaskLogDTO> getTaskLogsByTaskId(Long taskId, Long page, Long size, String status);

    //-----------------------------------------------------------------------------------------
    // flow
    //-----------------------------------------------------------------------------------------

    /**
     * create workflow
     * @param orgWorkflowDTO workflow info
     * @return workflow obj
     */
    OrgWorkflowDTO createWorkflow(OrgWorkflowDTO orgWorkflowDTO);

    /**
     * update work flow
     * @param orgWorkflowDTO workflow info
     * @return true or false
     */
    boolean updateWorkflow(OrgWorkflowDTO orgWorkflowDTO);

    /**
     * delete workflow, will delete all versions, version logs
     * @param orgWorkflowId id
     * @return true or false
     */
    boolean deleteWorkflow(Long orgWorkflowId);

    /**
     * get workflows by organization id
     * @param orgId id
     * @return workflows
     */
    List<OrgWorkflowDTO> getWorkflowsByOrgId(Long orgId);

    //-----------------------------------------------------------------------------------------
    // version
    //-----------------------------------------------------------------------------------------

    /**
     * create workflow version
     * @param orgWorkflowVersionDTO version info
     * @return version obj
     */
    OrgWorkflowVersionDTO createWorkflowVersion(OrgWorkflowVersionDTO orgWorkflowVersionDTO);

    /**
     * update workflow version
     * @param orgWorkflowVersionDTO version info
     * @return true or false
     */
    boolean updateWorkflowVersion(OrgWorkflowVersionDTO orgWorkflowVersionDTO);

    /**
     * delete workflow version, will delete all version logs.
     * @param orgWorkflowVersionId id
     * @return true or false
     */
    boolean deleteWorkflowVersion(Long orgWorkflowVersionId);

    /**
     * get workflow version
     * @param orgWorkflowVersionId id
     * @return version obj
     */
    OrgWorkflowVersionDTO getWorkflowVersion(Long orgWorkflowVersionId);

    /**
     * get workflow versions by workflow id
     * @param orgWorkFlowId id
     * @return versions
     */
    List<OrgWorkflowVersionDTO> getWorkflowVersionsByWorkflowId(Long orgWorkFlowId);

    //-----------------------------------------------------------------------------------------
    // exec log
    //-----------------------------------------------------------------------------------------

    /**
     * create workflow exec log info
     * @param orgWorkflowExecLogDTO exec log info
     * @return exec log obj
     */
    OrgWorkflowExecLogDTO createWorkflowExecLog(OrgWorkflowExecLogDTO orgWorkflowExecLogDTO);

    /**
     * get workflow exec log
     * @param orgWorkflowExecLogId id
     * @return exec log obj
     */
    OrgWorkflowExecLogDTO getWorkflowExecLog(Long orgWorkflowExecLogId);

    /**
     * get workflow exec logs by workflow version id
     * @param orgWorkflowVersionId id
     * @param page page
     * @param size size
     * @return page
     */
    PlatformPagaDTO<OrgWorkflowExecLogDTO> getWorkflowExecLogsByWorkflowVersionId(Long orgWorkflowVersionId, Long page, Long size);
}
