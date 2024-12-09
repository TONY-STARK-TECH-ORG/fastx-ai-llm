package com.fastx.ai.llm.platform.service.organization;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.api.IDubboOrganizationService;
import com.fastx.ai.llm.domains.api.IDubboTaskService;
import com.fastx.ai.llm.domains.api.IDubboToolService;
import com.fastx.ai.llm.domains.api.IDubboWorkflowService;
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

    @DubboReference
    IDubboWorkflowService workflowService;

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

    @Override
    @SentinelResource("org.workflow.create")
    public OrgWorkflowDTO createWorkflow(OrgWorkflowDTO orgWorkflowDTO) {
        Assert.notNull(orgWorkflowDTO, "orgWorkflowDTO is null");
        WorkflowDTO workflowDTO = new WorkflowDTO();
        BeanUtils.copyProperties(orgWorkflowDTO, workflowDTO);
        // create and return new
        OrgWorkflowDTO dto = new OrgWorkflowDTO();
        BeanUtils.copyProperties(workflowService.createWorkflow(workflowDTO), dto);
        return dto;
    }

    @Override
    @SentinelResource("org.workflow.update")
    public boolean updateWorkflow(OrgWorkflowDTO orgWorkflowDTO) {
        Assert.notNull(orgWorkflowDTO, "orgWorkflowDTO is null");
        WorkflowDTO workflowDTO = new WorkflowDTO();
        BeanUtils.copyProperties(orgWorkflowDTO, workflowDTO);
        return workflowService.updateWorkflow(workflowDTO);
    }

    @Override
    @SentinelResource("org.workflow.delete")
    public boolean deleteWorkflow(Long orgWorkflowId) {
        Assert.notNull(orgWorkflowId, "orgWorkflowId is null");
        return workflowService.deleteWorkflow(orgWorkflowId);
    }

    @Override
    @SentinelResource("org.workflow.get")
    public List<OrgWorkflowDTO> getWorkflowsByOrgId(Long orgId) {
        Assert.notNull(orgId, "orgId is null");
        List<WorkflowDTO> workflows = workflowService.getWorkflowsByOrganizationId(orgId);
        return Lists.emptyToNull(workflows).stream().map(w -> {
            OrgWorkflowDTO orgWorkflow = new OrgWorkflowDTO();
            BeanUtils.copyProperties(w, orgWorkflow);
            return orgWorkflow;
        }).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("org.workflow.version.create")
    public OrgWorkflowVersionDTO createWorkflowVersion(OrgWorkflowVersionDTO orgWorkflowVersionDTO) {
        Assert.notNull(orgWorkflowVersionDTO, "orgWorkflowVersionDTO is null");
        WorkflowVersionDTO workflowVersionDTO = new WorkflowVersionDTO();
        BeanUtils.copyProperties(orgWorkflowVersionDTO, workflowVersionDTO);
        // create and return new
        OrgWorkflowVersionDTO dto = new OrgWorkflowVersionDTO();
        BeanUtils.copyProperties(workflowService.createWorkflowVersion(workflowVersionDTO), dto);
        return dto;
    }

    @Override
    @SentinelResource("org.workflow.version.update")
    public boolean updateWorkflowVersion(OrgWorkflowVersionDTO orgWorkflowVersionDTO) {
        Assert.notNull(orgWorkflowVersionDTO, "orgWorkflowVersionDTO is null");
        WorkflowVersionDTO workflowVersionDTO = new WorkflowVersionDTO();
        BeanUtils.copyProperties(orgWorkflowVersionDTO, workflowVersionDTO);
        return workflowService.updateWorkflowVersion(workflowVersionDTO);
    }

    @Override
    @SentinelResource("org.workflow.version.delete")
    public boolean deleteWorkflowVersion(Long orgWorkflowVersionId) {
        Assert.notNull(orgWorkflowVersionId, "orgWorkflowVersionId is null");
        return workflowService.deleteWorkflowVersion(orgWorkflowVersionId);
    }

    @Override
    @SentinelResource("org.workflow.version.get")
    public OrgWorkflowVersionDTO getWorkflowVersion(Long orgWorkflowVersionId) {
        Assert.notNull(orgWorkflowVersionId, "orgWorkflowVersionId is null");
        // create and return dto
        OrgWorkflowVersionDTO orgWorkflowVersion = new OrgWorkflowVersionDTO();
        BeanUtils.copyProperties(workflowService.getWorkflowVersion(orgWorkflowVersionId), orgWorkflowVersion);
        return orgWorkflowVersion;
    }

    @Override
    @SentinelResource("org.workflow.version.get")
    public List<OrgWorkflowVersionDTO> getWorkflowVersionsByWorkflowId(Long orgWorkFlowId) {
        Assert.notNull(orgWorkFlowId, "orgWorkFlowId is null");
        List<WorkflowVersionDTO> workflowVersions = workflowService.getWorkflowVersionsByWorkflowId(orgWorkFlowId);
        return Lists.emptyToNull(workflowVersions).stream().map(w -> {
            OrgWorkflowVersionDTO orgWorkflowVersion = new OrgWorkflowVersionDTO();
            BeanUtils.copyProperties(w, orgWorkflowVersion);
            return orgWorkflowVersion;
        }).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("org.workflow.exec.log.create")
    public OrgWorkflowExecLogDTO createWorkflowExecLog(OrgWorkflowExecLogDTO orgWorkflowExecLogDTO) {
        Assert.notNull(orgWorkflowExecLogDTO, "orgWorkflowExecLogDTO is null");
        WorkflowExecLogDTO workflowExecLogDTO = new WorkflowExecLogDTO();
        BeanUtils.copyProperties(orgWorkflowExecLogDTO, workflowExecLogDTO);
        // create and return new
        OrgWorkflowExecLogDTO dto = new OrgWorkflowExecLogDTO();
        BeanUtils.copyProperties(workflowService.createWorkflowExecLog(workflowExecLogDTO), dto);
        return dto;
    }

    @Override
    @SentinelResource("org.workflow.exec.log.get")
    public OrgWorkflowExecLogDTO getWorkflowExecLog(Long orgWorkflowExecLogId) {
        Assert.notNull(orgWorkflowExecLogId, "orgWorkflowExecLogId is null");
        OrgWorkflowExecLogDTO dto = new OrgWorkflowExecLogDTO();
        BeanUtils.copyProperties(workflowService.getWorkflowExecLog(orgWorkflowExecLogId), dto);
        return dto;
    }

    @Override
    @SentinelResource("org.workflow.exec.log.get")
    public PlatformPagaDTO<OrgWorkflowExecLogDTO> getWorkflowExecLogsByWorkflowVersionId(
            Long orgWorkflowVersionId, Long page, Long size) {
        Assert.notNull(orgWorkflowVersionId, "orgWorkflowVersionId is null");
        Assert.notNull(page, "page is null");
        Assert.notNull(size, "size is null");

        PageDTO<WorkflowExecLogDTO> execLogs =
                workflowService.getWorkflowExecLogsByWorkflowVersionId(orgWorkflowVersionId, page, size);
        return PlatformPagaDTO.of(execLogs.getPage(), execLogs.getSize(), execLogs.getTotal(),
                execLogs.getList().stream().map(w -> {
                    OrgWorkflowExecLogDTO orgWorkflowExecLog = new OrgWorkflowExecLogDTO();
                    BeanUtils.copyProperties(w, orgWorkflowExecLog);
                    return orgWorkflowExecLog;
        }).collect(Collectors.toList()));
    }

}
