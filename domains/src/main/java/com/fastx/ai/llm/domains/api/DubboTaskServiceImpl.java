package com.fastx.ai.llm.domains.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    public PageDTO<TaskExecDTO> getTaskExecsByTaskId(Long taskId, Long page, Long size, String status) {
        Assert.notNull(taskId, "taskId is null");
        Assert.notNull(page, "page is null");
        Assert.notNull(size, "size is null");
        // search page
        Page<TaskExec> taskExecPage = taskExecService.getTaskExecs(taskId, page, size, status);
        return PageDTO.of(
                page,
                size,
                taskExecPage.getTotal(),
                taskExecPage.getRecords().stream().map(TaskExec::to).toList());
    }

    @Override
    public PageDTO<TaskExecDTO> getTaskExecs(Long page, Long size, String status) {
        Assert.notNull(page, "page is null");
        Assert.notNull(size, "size is null");
        // search page
        Page<TaskExec> taskExecPage = taskExecService.getTaskExecs(null, page, size, status);
        return PageDTO.of(
                page,
                size,
                taskExecPage.getTotal(),
                taskExecPage.getRecords().stream().map(TaskExec::to).toList());
    }

    @Override
    public List<TaskNodeExecDTO> createTaskExecNodes(List<TaskNodeExecDTO> taskNodeExecDTOList) {
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
    public Boolean updateTaskExecNodes(List<TaskNodeExecDTO> taskNodeExecDTOList) {
        Assert.isTrue(taskNodeExecDTOList.stream().allMatch(t ->
            isValidated(t) && Objects.nonNull(t.getId())
        ), "task not dto basic info not validated.");
        List<TaskNodeExec> taskNodeExecs =
                taskNodeExecDTOList.stream().map(TaskNodeExec::of).toList();
        Assert.isTrue(
                taskNodeExecService.updateBatchById(taskNodeExecs),
                "update task node exec failed!"
        );
        return null;
    }

    @Override
    public List<TaskNodeExecDTO> getTaskExecNodes(Long taskExecId) {
        List<TaskNodeExec> taskExecNodes = taskNodeExecService.getTaskExecNodes(taskExecId);
        return taskExecNodes.stream().map(TaskNodeExec::to).toList();
    }

    @Override
    public PageDTO<TaskNodeExecDTO> getTaskExecNodes(
            Long page, Long size, String status, Boolean checkPrevNodes) {
        Assert.notNull(page, "page is null");
        Assert.notNull(size, "size is null");
        Page<TaskNodeExec> taskNodeExecPage =
                taskNodeExecService.getTaskExecNodes(page, size, status, checkPrevNodes);
        return PageDTO.of(
                page,
                size,
                taskNodeExecPage.getTotal(),
                taskNodeExecPage.getRecords().stream().map(TaskNodeExec::to).toList());
    }

    private void isValidated(TaskDTO taskDTO) {
        Assert.notNull(taskDTO, "task is null");
        Assert.notNull(taskDTO.getOrganizationId(), "organizationId is null");
        Assert.hasText(taskDTO.getName(), "name is null");
        Assert.hasText(taskDTO.getDescription(), "description is null");
        Assert.hasText(taskDTO.getCron(), "cron is null");
        Assert.notNull(taskDTO.getWorkflowId(), "workflow is null");
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
