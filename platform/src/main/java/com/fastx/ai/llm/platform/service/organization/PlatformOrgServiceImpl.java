package com.fastx.ai.llm.platform.service.organization;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.api.IDubboOrganizationService;
import com.fastx.ai.llm.domains.api.IDubboTaskService;
import com.fastx.ai.llm.domains.api.IDubboToolService;
import com.fastx.ai.llm.domains.dto.*;
import com.fastx.ai.llm.platform.api.IPlatformOrgService;
import com.fastx.ai.llm.platform.dto.*;
import com.rometools.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author stark
 */
@DubboService
public class PlatformOrgServiceImpl implements IPlatformOrgService {

    @DubboReference
    IDubboOrganizationService organizationService;

    @DubboReference
    IDubboToolService toolService;

    @DubboReference
    IDubboTaskService taskService;

    @Override
    @SentinelResource("org.getBy.id")
    public OrgDTO getOrgById(Long orgId) {
        OrganizationDTO organization = organizationService.findByOrganizationId(orgId);
        Assert.notNull(organization, "organization not found");
        OrgDTO org = new OrgDTO();
        BeanUtils.copyProperties(organization, org);
        return org;
    }

    @Override
    @SentinelResource("org.getBy.userId")
    public List<OrgDTO> getOrgByUserId(Long userId) {
        List<OrganizationDTO> organizationList = organizationService.findByUserId(userId);
        return Lists.emptyToNull(organizationList).stream().map(o -> {
            OrgDTO org = new OrgDTO();
            BeanUtils.copyProperties(o, org);
            return org;
        }).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("org.tool.create")
    public OrgToolDTO createOrgTools(OrgToolDTO orgToolDTO) {
        Assert.notNull(orgToolDTO, "orgTool is null");
        // create
        OrganizationToolsDTO organizationToolsDTO = new OrganizationToolsDTO();
        BeanUtils.copyProperties(orgToolDTO, organizationToolsDTO);
        // create dto
        OrgToolDTO dto = new OrgToolDTO();
        BeanUtils.copyProperties(toolService.createOrganizationTools(organizationToolsDTO), dto);
        return dto;
    }

    @Override
    @SentinelResource("org.tool.update")
    public boolean updateOrgTools(OrgToolDTO orgToolsDTO) {
        Assert.notNull(orgToolsDTO, "orgTools is null");
        Assert.notNull(orgToolsDTO.getId(), "orgTools id is null");
        OrganizationToolsDTO organizationToolsDTO = new OrganizationToolsDTO();
        BeanUtils.copyProperties(orgToolsDTO, organizationToolsDTO);
        return toolService.updateOrganizationTools(organizationToolsDTO);
    }

    @Override
    @SentinelResource("org.tool.delete")
    public boolean deleteOrgTools(Long id) {
        Assert.notNull(id, "id is null");
        return toolService.deleteOrganizationTools(id);
    }

    @Override
    @SentinelResource("org.tool.get")
    public List<OrgToolDTO> getOrgTools(Long orgId) {
        Assert.notNull(orgId, "orgId is null");
        List<OrganizationToolsDTO> organizationTools = Lists.emptyToNull(
                toolService.getOrganizationTools(orgId));
        return Lists.emptyToNull(organizationTools).stream().map(ogt -> {
            OrgToolDTO orgTool = new OrgToolDTO();
            BeanUtils.copyProperties(ogt, orgTool);
            return orgTool;
        }).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("org.task.create")
    public OrgTaskDTO createTask(OrgTaskDTO taskDTO) {
        Assert.notNull(taskDTO, "taskDTO is null");
        TaskDTO task = new TaskDTO();
        // create a task
        BeanUtils.copyProperties(taskDTO, task);
        // return new obj
        OrgTaskDTO dto = new OrgTaskDTO();
        BeanUtils.copyProperties(taskService.createTask(task), dto);
        return dto;
    }

    @Override
    @SentinelResource("org.task.update")
    public boolean updateTask(OrgTaskDTO taskDTO) {
        Assert.notNull(taskDTO, "taskDTO is null");
        TaskDTO task = new TaskDTO();
        // create a task
        BeanUtils.copyProperties(taskDTO, task);
        return taskService.updateTask(task);
    }

    @Override
    @SentinelResource("org.task.delete")
    public boolean deleteTask(Long taskId) {
        Assert.notNull(taskId, "taskId is null");
        return taskService.deleteTask(taskId);
    }

    @Override
    @SentinelResource("org.task.get")
    public List<OrgTaskDTO> getTasksByOrgId(Long organizationId) {
        Assert.notNull(organizationId, "organizationId is null");
        List<TaskDTO> tasks = taskService.getTasksByOrganizationId(organizationId);
        return Lists.emptyToNull(tasks).stream().map(t -> {
            OrgTaskDTO orgTask = new OrgTaskDTO();
            BeanUtils.copyProperties(t, orgTask);
            return orgTask;
        }).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("org.task.log.create")
    public OrgTaskLogDTO createTaskLog(OrgTaskLogDTO taskLogDTO) {
        Assert.notNull(taskLogDTO, "taskLogDTO is null");
        // create dto
        TaskLogDTO taskLog = new TaskLogDTO();
        BeanUtils.copyProperties(taskLogDTO, taskLog);
        // return new task obj
        OrgTaskLogDTO orgTaskLog = new OrgTaskLogDTO();
        BeanUtils.copyProperties(taskService.createTaskLog(taskLog), orgTaskLog);
        return orgTaskLog;
    }

    @Override
    @SentinelResource("org.task.log.get")
    public PlatformPagaDTO<OrgTaskLogDTO> getTaskLogsByTaskId(Long taskId, Long page, Long size, String status) {
        PageDTO<TaskLogDTO> pageTaskLogs = taskService.getTaskLogsByTaskId(taskId, page, size, status);
        return PlatformPagaDTO.of(page, size, pageTaskLogs.getTotal(), pageTaskLogs.getList().stream().map(t -> {
            OrgTaskLogDTO orgTaskLog = new OrgTaskLogDTO();
            BeanUtils.copyProperties(t, orgTaskLog);
            return orgTaskLog;
        }).collect(Collectors.toList()));
    }

}
