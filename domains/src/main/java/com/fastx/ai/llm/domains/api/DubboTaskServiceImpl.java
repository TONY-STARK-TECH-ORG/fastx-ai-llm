package com.fastx.ai.llm.domains.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.dto.PageDTO;
import com.fastx.ai.llm.domains.dto.TaskDTO;
import com.fastx.ai.llm.domains.dto.TaskLogDTO;
import com.fastx.ai.llm.domains.entity.Task;
import com.fastx.ai.llm.domains.entity.TaskLog;
import com.fastx.ai.llm.domains.service.ITaskLogService;
import com.fastx.ai.llm.domains.service.ITaskService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

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
    ITaskLogService taskLogService;

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
    public boolean deleteTask(Long taskId) {
        Assert.notNull(taskId, "taskId is null");
        return taskService.removeById(taskId);
    }

    @Override
    @SentinelResource("task.get")
    public List<TaskDTO> getTasksByOrganizationId(Long organizationId) {
        Assert.notNull(organizationId, "organizationId is null");
        List<Task> taskList = taskService.getTasksByOrganizationId(organizationId);
        return taskList.stream().map(Task::to).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("task.log.create")
    public TaskLogDTO createTaskLog(TaskLogDTO taskLogDTO) {
        isValidated(taskLogDTO);
        taskLogDTO.setStatus(IConstant.WAIT);
        TaskLog taskLog = TaskLog.of(taskLogDTO);
        Assert.isTrue(taskLogService.save(taskLog), "save task log failed!");
        return taskLog.to();
    }

    @Override
    @SentinelResource("task.log.get")
    public PageDTO<TaskLogDTO> getTaskLogsByTaskId(Long taskId, Long page, Long size, String status) {
        Assert.notNull(taskId, "taskId is null");
        Assert.notNull(page, "page is null");
        Assert.notNull(size, "size is null");
        // search page
        Page<TaskLog> taskLogPage = taskLogService.getTaskLogsByTaskId(taskId, page, size, status);
        return PageDTO.of(
                page,
                size,
                taskLogPage.getTotal(),
                taskLogPage.getRecords().stream().map(TaskLog::to).collect(Collectors.toList()));
    }

    private void isValidated(TaskDTO taskDTO) {
        Assert.notNull(taskDTO, "task is null");
        Assert.notNull(taskDTO.getOrganizationId(), "organizationId is null");
        Assert.hasText(taskDTO.getName(), "name is null");
        Assert.hasText(taskDTO.getDescription(), "description is null");
        Assert.hasText(taskDTO.getCron(), "cron is null");
        Assert.notNull(taskDTO.getWorkflowId(), "workflowId is null");
    }

    private void isValidated(TaskLogDTO taskLogDTO) {
        Assert.notNull(taskLogDTO, "taskLog is null");
        Assert.notNull(taskLogDTO.getTaskId(), "taskId is null");
    }

}
