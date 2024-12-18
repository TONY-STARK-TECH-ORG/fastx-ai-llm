package com.fastx.ai.llm.domains.api;

import com.fastx.ai.llm.domains.dto.PageDTO;
import com.fastx.ai.llm.domains.dto.TaskDTO;
import com.fastx.ai.llm.domains.dto.TaskExecDTO;
import com.fastx.ai.llm.domains.dto.TaskNodeExecDTO;

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
     * delete a task, will delete all task execs.
     * @param taskId task id
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
     * create new task exec
     * @param taskExecDTO exec info
     * @return created task exec
     */
    TaskExecDTO createTaskExec(TaskExecDTO taskExecDTO);

    /**
     * all task logs
     * @param taskId task id
     * @param page page
     * @param size size
     * @param status status
     * @return task list with page info
     */
    PageDTO<TaskExecDTO> getTaskExecsByTaskId(Long taskId, Long page, Long size, String status);

    /**
     * all task logs
     * @param page page
     * @param size size
     * @param status status
     * @return task list with page info
     */
    PageDTO<TaskExecDTO> getTaskExecs(Long page, Long size, String status);

    //--------------------------------------------------
    // task node exec.
    //--------------------------------------------------

    /**
     * create a new task exec for run.
     * @param taskNodeExecDTOList task node exec dto list.
     * @return update result.
     */
    List<TaskNodeExecDTO> createTaskExecNodes(List<TaskNodeExecDTO> taskNodeExecDTOList);

    /**
     * update task node exec status.
     * @param taskNodeExecDTOList task node exec dto list.
     * @return update result.
     */
    Boolean updateTaskExecNodes(List<TaskNodeExecDTO> taskNodeExecDTOList);

    /**
     * get all task node exec by task id.
     * @param taskExecId task exec id.
     * @return taskId
     */
    List<TaskNodeExecDTO> getTaskExecNodes(Long taskExecId);

    /**
     * query task exec nodes with condition.
     * @param page page
     * @param size size
     * @param status node exec status
     * @param checkPrevNodes check prev node execute state == 'finish'
     * @return result
     */
    PageDTO<TaskNodeExecDTO> getTaskExecNodes(
            Long page,
            Long size,
            String status,
            Boolean checkPrevNodes
    );
}
