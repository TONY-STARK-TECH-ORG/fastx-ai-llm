package com.fastx.ai.llm.domains.api;

import com.fastx.ai.llm.domains.dto.PageDTO;
import com.fastx.ai.llm.domains.dto.TaskDTO;
import com.fastx.ai.llm.domains.dto.TaskLogDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IDubboTaskService {

    /**
     * create a new task
     * @param taskDTO task info
     * @return created task
     */
    TaskDTO createTask(TaskDTO taskDTO);

    /**
     * update a task
     * @param taskDTO task info
     * @return true or false
     */
    boolean updateTask(TaskDTO taskDTO);

    /**
     * delete a task, will delete all task log.
     * @param taskDTO task info
     * @return true or false
     */
    boolean deleteTask(Long taskId);

    /**
     * get all task under organization
     * @param organizationId organization id
     * @return task list
     */
    List<TaskDTO> getTasksByOrganizationId(Long organizationId);

    /**
     * create new task log
     * @param taskLogDTO log info
     * @return created task log
     */
    TaskLogDTO createTaskLog(TaskLogDTO taskLogDTO);

    /**
     * all task logs
     * @param taskId task id
     * @param page page
     * @param size size
     * @param status status
     * @return task list with page info
     */
    PageDTO<TaskLogDTO> getTaskLogsByTaskId(Long taskId, Long page, Long size, String status);

}
