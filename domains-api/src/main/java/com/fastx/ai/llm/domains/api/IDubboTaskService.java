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
     * get task byId
     * @param taskId task id
     * @return single task
     */
    TaskDTO getTaskById(Long taskId);

    //--------------------------------------------------
    // task exec.
    //--------------------------------------------------

    /**
     * get single task exec by id
     * @param taskExecId task exec id
     * @return task exec
     */
    TaskExecDTO getTaskExecById(Long taskExecId);

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
     * @param type type
     * @return task list with page info
     */
    PageDTO<TaskExecDTO> getTaskExecsByTaskId(Long taskId, Long page, Long size, String status, String type);

    /**
     * all task logs
     * @param page page
     * @param size size
     * @param status status
     * @param type type
     * @return task list with page info
     */
    PageDTO<TaskExecDTO> getTaskExecs(Long page, Long size, String status, String type);

    /**
     * update task exec state
     * @param taskExecDTO task exec
     * @return true or false.
     */
    Boolean updateTaskExec(TaskExecDTO taskExecDTO);

    //--------------------------------------------------
    // task node exec.
    //--------------------------------------------------

    /**
     * create a new task exec for run.
     * @param taskNodeExecDTOList task node exec dto list.
     * @return update result.
     */
    List<TaskNodeExecDTO> createTaskNodeExecs(List<TaskNodeExecDTO> taskNodeExecDTOList);

    /**
     * update task node exec status.
     * @param taskNodeExecDTOList task node exec dto list.
     * @return update result.
     */
    Boolean updateTaskNodeExecs(List<TaskNodeExecDTO> taskNodeExecDTOList);

    /**
     * update task node exec status. (with lock!)
     * @param taskNodeExecDTOList task node exec dto list.
     * @return update result.
     */
    Boolean updateTaskNodeExecs(TaskNodeExecDTO taskNodeExecDTOList);

    /**
     * get all task node exec by task id.
     * @param taskExecId task exec id.
     * @return taskId
     */
    List<TaskNodeExecDTO> getTaskNodeExecs(Long taskExecId);

    /**
     * query task exec nodes with condition.
     * @param page page
     * @param size size
     * @param status node exec status
     * @param checkPrevNodes check prev node execute state == 'finish'
     * @return result
     */
    PageDTO<TaskNodeExecDTO> getTaskNodeExecs(
            Long page,
            Long size,
            String status,
            Boolean checkPrevNodes
    );

    /**
     * delete task node execs before by execId
     * @param taskExecId task exec id
     */
    boolean deleteTaskNodeExecs(Long taskExecId);

    /**
     * 根据 nodeId 获取所有父链路节点
     * @param nodeId
     * @return
     */
    List<TaskNodeExecDTO> getParentChainTaskNodeExecByNodeId(String nodeId);

    /**
     * 判断所有父节点是否已经执行完成
     * @param nodeId
     * @return
     */
    boolean isParentTaskNodeFinished(String nodeId);

    Boolean isTaskExecNodeFinished(Long taskExecId);
}
