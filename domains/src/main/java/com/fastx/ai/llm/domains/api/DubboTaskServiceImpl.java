package com.fastx.ai.llm.domains.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastx.ai.llm.domains.config.lock.RedisLock;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.dto.PageDTO;
import com.fastx.ai.llm.domains.dto.TaskDTO;
import com.fastx.ai.llm.domains.dto.TaskExecDTO;
import com.fastx.ai.llm.domains.dto.TaskNodeExecDTO;
import com.fastx.ai.llm.domains.entity.Task;
import com.fastx.ai.llm.domains.entity.TaskExec;
import com.fastx.ai.llm.domains.entity.TaskNodeExec;
import com.fastx.ai.llm.domains.service.ITaskExecService;
import com.fastx.ai.llm.domains.service.ITaskNodeExecService;
import com.fastx.ai.llm.domains.service.ITaskService;
import com.rometools.utils.Lists;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
@DubboService
public class DubboTaskServiceImpl extends DubboBaseDomainService implements IDubboTaskService {

    @Autowired
    ITaskService taskService;

    @Autowired
    ITaskExecService taskExecService;

    @Autowired
    ITaskNodeExecService taskNodeExecService;

    @Override
    @SentinelResource("task.create")
    public TaskDTO createTask(TaskDTO taskDTO) {
        isValidated(taskDTO);
        // default set to inactive.
        taskDTO.setStatus(IConstant.IN_ACTIVE);
        Task task = Task.of(taskDTO);
        Assert.isTrue(taskService.save(task), "save task failed!");
        return task.to();
    }

    @Override
    @SentinelResource("task.update")
    public boolean updateTask(TaskDTO taskDTO) {
        isValidated(taskDTO);
        Assert.notNull(taskDTO.getId(), "id is null");
        return taskService.updateById(Task.of(taskDTO));
    }

    @Override
    @SentinelResource("task.delete")
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTask(Long taskId) {
        Assert.notNull(taskId, "taskId is null");
        Assert.isTrue(taskService.removeById(taskId), "delete task failed!");
        Assert.isTrue(taskExecService.removeExecsByTaskId(taskId), "delete log failed!");
        return true;
    }

    @Override
    @SentinelResource("task.get")
    public List<TaskDTO> getTasksByOrganizationId(Long organizationId) {
        Assert.notNull(organizationId, "organizationId is null");
        List<Task> taskList = taskService.getTasksByOrganizationId(organizationId);
        return taskList.stream().map(Task::to).toList();
    }

    @Override
    @SentinelResource("task.get")
    public TaskDTO getTaskById(Long taskId) {
        Assert.notNull(taskId, "taskId is null");
        Task task = taskService.getById(taskId);
        Assert.notNull(task, "task is null");
        return task.to();
    }

    @Override
    @SentinelResource("task.exec.get")
    public TaskExecDTO getTaskExecById(Long taskExecId) {
        Assert.notNull(taskExecId, "taskExecId is null");
        TaskExec taskExec = taskExecService.getById(taskExecId);
        Assert.notNull(taskExec, "taskExec is null");
        return taskExec.to();
    }

    @Override
    @SentinelResource("task.exec.create")
    public TaskExecDTO createTaskExec(TaskExecDTO taskExecDTO) {
        isValidated(taskExecDTO);
        taskExecDTO.setStatus(IConstant.WAIT);
        TaskExec taskExec = TaskExec.of(taskExecDTO);
        Assert.isTrue(taskExecService.save(taskExec), "save task exec failed!");
        return taskExec.to();
    }

    @Override
    @SentinelResource("task.exec.get")
    public PageDTO<TaskExecDTO> getTaskExecsByTaskId(Long taskId, Long page, Long size, String status, String type) {
        Assert.notNull(taskId, "taskId is null");
        Assert.notNull(page, "page is null");
        Assert.notNull(size, "size is null");
        // search page
        Page<TaskExec> taskExecPage = taskExecService.getTaskExecs(taskId, page, size, status, type);
        return PageDTO.of(
                page,
                size,
                taskExecPage.getTotal(),
                taskExecPage.getRecords().stream().map(TaskExec::to).toList());
    }

    @Override
    @SentinelResource("task.exec.get")
    public PageDTO<TaskExecDTO> getTaskExecs(Long page, Long size, String status, String type) {
        Assert.notNull(page, "page is null");
        Assert.notNull(size, "size is null");
        // search page
        Page<TaskExec> taskExecPage = taskExecService.getTaskExecs(null, page, size, status, type);
        return PageDTO.of(
                page,
                size,
                taskExecPage.getTotal(),
                taskExecPage.getRecords().stream().map(TaskExec::to).toList());
    }

    @Override
    @SentinelResource("task.exec.update")
    @RedisLock(key = "taskExecLock::${#taskExecDTO.id}")
    public Boolean updateTaskExec(TaskExecDTO taskExecDTO) {
        isValidated(taskExecDTO);
        Assert.notNull(taskExecDTO.getId(), "id is null");
        if (IConstant.RUNNING.equals(taskExecDTO.getStatus())) {
            TaskExec taskExec = taskExecService.getById(taskExecDTO.getId());
            // check states not WAIT now
            Assert.isTrue(
                    IConstant.WAIT.equals(taskExec) || IConstant.ERROR.equals(taskExec),
                    "update task exec status failed! not (WAIT, ERROR) now!"
            );
        }
        return taskExecService.updateById(TaskExec.of(taskExecDTO));
    }

    @Override
    @SentinelResource("task.exec.node.create")
    @Transactional(rollbackFor = Exception.class)
    public List<TaskNodeExecDTO> createTaskNodeExecs(List<TaskNodeExecDTO> taskNodeExecDTOList) {
        Assert.isTrue(taskNodeExecDTOList.stream().allMatch(this::isValidated), "task not dto basic info not validated.");
        List<TaskNodeExec> taskNodeExecs =
                taskNodeExecDTOList.stream().map(TaskNodeExec::of).toList();
        Assert.isTrue(
                taskNodeExecService.saveBatch(taskNodeExecs),
                "save task node exec failed!"
        );
        return taskNodeExecs.stream().map(TaskNodeExec::to).toList();
    }

    @Override
    @SentinelResource("task.exec.node.update")
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateTaskNodeExecs(List<TaskNodeExecDTO> taskNodeExecDTOList) {
        Assert.isTrue(taskNodeExecDTOList.stream().allMatch(t ->
            isValidated(t) && Objects.nonNull(t.getId())
        ), "task not dto basic info not validated.");
        List<TaskNodeExec> taskNodeExecs =
                taskNodeExecDTOList.stream().map(TaskNodeExec::of).toList();
        return taskNodeExecService.updateBatchById(taskNodeExecs);
    }

    @Override
    @RedisLock(key = "taskNodeExecLock::${#taskNodeExecDTO.id}")
    public Boolean updateTaskNodeExecs(TaskNodeExecDTO taskNodeExecDTO) {
        Assert.isTrue(isValidated(taskNodeExecDTO), "task not dto basic info not validated.");
        Assert.notNull(taskNodeExecDTO.getId(), "id is null");
        if (IConstant.RUNNING.equals(taskNodeExecDTO.getStatus())) {
            // validate status was wait now.
            TaskNodeExec nodeExec = taskNodeExecService.getById(taskNodeExecDTO.getId());
            Assert.isTrue(
                    IConstant.WAIT.equals(nodeExec.getStatus()) || IConstant.ERROR.equals(nodeExec.getStatus()),
                    "task node exec status not validated (WAIT, ERROR) now!"
            );
        }
        TaskNodeExec taskNodeExec = TaskNodeExec.of(taskNodeExecDTO);
        return taskNodeExecService.updateById(taskNodeExec);
    }

    @Override
    @SentinelResource("task.exec.node.get")
    public List<TaskNodeExecDTO> getTaskNodeExecs(Long taskExecId) {
        List<TaskNodeExec> TaskNodeExecs = taskNodeExecService.getTaskNodeExecs(taskExecId);
        return TaskNodeExecs.stream().map(TaskNodeExec::to).toList();
    }

    @Override
    @SentinelResource("task.exec.node.get")
    public PageDTO<TaskNodeExecDTO> getTaskNodeExecs(
            Long page, Long size, String status, Boolean checkPrevNodes) {
        Assert.notNull(page, "page is null");
        Assert.notNull(size, "size is null");
        Page<TaskNodeExec> taskNodeExecPage =
                taskNodeExecService.getTaskNodeExecs(page, size, status, checkPrevNodes);
        return PageDTO.of(
                page,
                size,
                taskNodeExecPage.getTotal(),
                taskNodeExecPage.getRecords().stream().map(TaskNodeExec::to).toList());
    }

    @Override
    @SentinelResource("task.exec.node.remove")
    public boolean deleteTaskNodeExecs(Long taskExecId) {
        Assert.notNull(taskExecId, "taskExecId is null");
        return taskNodeExecService.removeByExecId(taskExecId);
    }

    @Override
    @SentinelResource("task.exec.node.get")
    public List<TaskNodeExecDTO> getParentChainTaskNodeExecByNodeId(String nodeId) {
        Assert.notNull(nodeId, "nodeId is null");
        List<TaskNodeExec> parentChainTaskNodeExec = taskNodeExecService.getParentChainTaskNodeExecByNodeId(nodeId);
        return Lists.createWhenNull(parentChainTaskNodeExec.stream().map(TaskNodeExec::to).toList());
    }

    @Override
    public boolean isParentTaskNodeFinished(String nodeId) {
        Assert.notNull(nodeId, "nodeId is null");
        List<TaskNodeExec> lists = taskNodeExecService.getTaskNodeExecs(nodeId);
        //判断阶段存不存在
        Assert.isTrue(!lists.isEmpty(), "isParentTaskNodeFinished current node is null");
        List<TaskNodeExec> parentTaskNodeExec = taskNodeExecService.getParentTaskNodeExec(nodeId);
        return parentTaskNodeExec.stream().allMatch(t -> IConstant.FINISH.equals(t.getStatus()));
    }

    @Override
    public Boolean isTaskExecNodeFinished(Long taskExecId) {
        Assert.notNull(taskExecId, "taskExecId is null");
        Assert.notNull(taskExecService.getById(taskExecId), "isTaskExecNodeFinished task exec is null");
        List<TaskNodeExec> TaskNodeExecs = taskNodeExecService.getTaskNodeExecs(taskExecId);
        Assert.notEmpty(TaskNodeExecs, "isTaskExecNodeFinished task node execs is empty");
        return TaskNodeExecs.stream().allMatch(t -> IConstant.FINISH.equals(t.getStatus()));
    }

    private void isValidated(TaskDTO taskDTO) {
        Assert.notNull(taskDTO, "task is null");
        Assert.notNull(taskDTO.getOrganizationId(), "organizationId is null");
        Assert.hasText(taskDTO.getName(), "name is null");
        Assert.hasText(taskDTO.getDescription(), "description is null");
        Assert.hasText(taskDTO.getCron(), "cron is null");
        Assert.notNull(taskDTO.getWorkflowVersionId(), "workflow version is null");
        Assert.notNull(taskDTO.getType(), "type is null");
    }

    private void isValidated(TaskExecDTO taskExecDTO) {
        Assert.notNull(taskExecDTO, "taskLog is null");
        Assert.notNull(taskExecDTO.getTaskId(), "taskId is null");
    }

    private boolean isValidated(TaskNodeExecDTO taskNodeExecDTO) {
        Assert.notNull(taskNodeExecDTO, "taskNodeExec is null");
        Assert.notNull(taskNodeExecDTO.getTaskExecId(), "taskExecId is null");
        Assert.notNull(taskNodeExecDTO.getNodeId(), "nodeId is null");
        return true;
    }

}
