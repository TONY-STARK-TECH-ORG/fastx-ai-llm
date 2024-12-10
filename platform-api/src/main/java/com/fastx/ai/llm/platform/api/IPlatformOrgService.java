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
     * list all tools in organization
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
}
