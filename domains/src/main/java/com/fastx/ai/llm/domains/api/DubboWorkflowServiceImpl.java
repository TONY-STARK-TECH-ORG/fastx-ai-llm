package com.fastx.ai.llm.domains.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.dto.PageDTO;
import com.fastx.ai.llm.domains.dto.WorkflowDTO;
import com.fastx.ai.llm.domains.dto.WorkflowExecLogDTO;
import com.fastx.ai.llm.domains.dto.WorkflowVersionDTO;
import com.fastx.ai.llm.domains.entity.Workflow;
import com.fastx.ai.llm.domains.entity.WorkflowExecLog;
import com.fastx.ai.llm.domains.entity.WorkflowVersion;
import com.fastx.ai.llm.domains.service.IWorkflowExecLogService;
import com.fastx.ai.llm.domains.service.IWorkflowService;
import com.fastx.ai.llm.domains.service.IWorkflowVersionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
public class DubboWorkflowServiceImpl extends DubboBaseDomainService implements IDubboWorkflowService {

    @Autowired
    IWorkflowService workflowService;

    @Autowired
    IWorkflowVersionService workflowVersionService;

    @Autowired
    IWorkflowExecLogService workflowExecLogService;

    @Override
    @SentinelResource("workflow.create")
    public WorkflowDTO createWorkflow(WorkflowDTO workflowDTO) {
        isValidated(workflowDTO);
        // default set to inactive
        workflowDTO.setStatus(IConstant.IN_ACTIVE);
        Workflow workflow = Workflow.of(workflowDTO);
        Assert.isTrue(workflowService.save(workflow), "save workflow failed!");
        return workflow.to();
    }

    @Override
    @SentinelResource("workflow.update")
    public boolean updateWorkflow(WorkflowDTO workflowDTO) {
        isValidated(workflowDTO);
        Assert.notNull(workflowDTO.getId(), "id is null");
        Workflow workflow = Workflow.of(workflowDTO);
        return workflowService.updateById(workflow);
    }

    @Override
    @SentinelResource("workflow.delete")
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteWorkflow(Long workflowId) {
        Assert.notNull(workflowId, "workflow id was null");
        // delete workflow
        Assert.isTrue(workflowService.removeById(workflowId), "delete workflow failed");
        // delete workflow version
        List<WorkflowVersion> versions = workflowVersionService.getWorkflowVersionByWorkflowId(workflowId);
        if (CollectionUtils.isNotEmpty(versions)) {
            List<Long> versionIds = versions.stream().map(WorkflowVersion::getId).toList();
            Assert.isTrue(workflowVersionService.removeByIds(versionIds), "delete workflow version failed");
            // remove exec logs
            Assert.isTrue(workflowExecLogService.removeWorkflowExecLogByWorkflowVersionIds(versionIds),
                    "delete workflow exec log failed");
        }
        return true;
    }

    @Override
    @SentinelResource("workflow.get")
    public List<WorkflowDTO> getWorkflowsByOrganizationId(Long organizationId) {
        List<Workflow> workflows = workflowService.getWorkflowsByOrganizationId(organizationId);
        return workflows.stream().map(Workflow::to).toList();
    }

    @Override
    @SentinelResource("workflow.version.create")
    public WorkflowVersionDTO createWorkflowVersion(WorkflowVersionDTO workflowVersionDTO) {
        isValidated(workflowVersionDTO);
        WorkflowVersion workflowVersion = WorkflowVersion.of(workflowVersionDTO);
        Assert.isTrue(workflowVersionService.save(workflowVersion), "save workflow version failed");
        return workflowVersion.to();
    }

    @Override
    @SentinelResource("workflow.version.update")
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWorkflowVersion(WorkflowVersionDTO workflowVersionDTO) {
        isValidated(workflowVersionDTO);
        Assert.notNull(workflowVersionDTO.getId(), "id was null");
        if (IConstant.ACTIVE.equals(workflowVersionDTO.getStatus())) {
            // set other version to inactive.
            Assert.isTrue(
                    workflowVersionService.setOtherVersionToInactive(workflowVersionDTO.getWorkflowId()), "inactive other version failed.");
        }
        WorkflowVersion workflowVersion = WorkflowVersion.of(workflowVersionDTO);
        Assert.isTrue(workflowVersionService.updateById(workflowVersion), "update work flow version failed!");
        return true;
    }

    @Override
    @SentinelResource("workflow.version.delete")
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteWorkflowVersion(Long workflowVersionId) {
        Assert.notNull(workflowVersionId, "workflow version id was null");
        Assert.isTrue(workflowVersionService.removeById(workflowVersionId), "delete workflow version failed");
        Assert.isTrue(workflowExecLogService
                .removeWorkflowExecLogByWorkflowVersionIds(List.of(workflowVersionId)),"delete workflow exec log failed");
        return true;
    }

    @Override
    @SentinelResource("workflow.version.get")
    public WorkflowVersionDTO getWorkflowVersion(Long workflowVersionId) {
        Assert.notNull(workflowVersionId, "workflow version id was null");
        WorkflowVersion workflowVersion = workflowVersionService.getById(workflowVersionId);
        Assert.notNull(workflowVersion, "not found workflow version");
        return workflowVersion.to();
    }

    @Override
    @SentinelResource("workflow.version.get")
    public List<WorkflowVersionDTO> getWorkflowVersionsByWorkflowId(Long workFlowId) {
        Assert.notNull(workFlowId, "workflow id was null");
        List<WorkflowVersion> workflowVersions = workflowVersionService.getWorkflowVersionByWorkflowId(workFlowId);
        return workflowVersions.stream().map(WorkflowVersion::to).toList();
    }

    @Override
    @SentinelResource("workflow.exec.log.create")
    public WorkflowExecLogDTO createWorkflowExecLog(WorkflowExecLogDTO workflowExecLogDTO) {
        isValidated(workflowExecLogDTO);
        WorkflowExecLog workflowExecLog = WorkflowExecLog.of(workflowExecLogDTO);
        Assert.isTrue(workflowExecLogService.save(workflowExecLog), "save workflow exec log failed");
        return workflowExecLog.to();
    }

    @Override
    @SentinelResource("workflow.exec.log.get")
    public WorkflowExecLogDTO getWorkflowExecLog(Long workflowExecLogId) {
        Assert.notNull(workflowExecLogId, "workflow exec log id was null");
        WorkflowExecLog workflowExecLog = workflowExecLogService.getById(workflowExecLogId);
        Assert.notNull(workflowExecLog, "not found workflow exec log");
        return workflowExecLog.to();
    }

    @Override
    @SentinelResource("workflow.exec.log.get")
    public PageDTO<WorkflowExecLogDTO> getWorkflowExecLogsByWorkflowVersionId(Long workflowVersionId, Long page, Long size) {
        Assert.notNull(workflowVersionId, "workflow version id was null");
        Assert.notNull(page, "page was null");
        Assert.notNull(size, "size was null");
        Page<WorkflowExecLog> execLogs =
                workflowExecLogService.getWorkflowExecLogsByWorkflowVersionId(workflowVersionId, page, size);
        return PageDTO.of(execLogs.getCurrent(), execLogs.getSize(), execLogs.getTotal(),
                execLogs.getRecords().stream().map(WorkflowExecLog::to).toList());
    }

    private void isValidated(WorkflowDTO workflow) {
        Assert.notNull(workflow, "workflow must not be null");
        Assert.notNull(workflow.getOrganizationId(), "organization id was null");
        Assert.hasText(workflow.getName(), "name must not be empty");
    }

    private void isValidated(WorkflowVersionDTO workflowVersion) {
        Assert.notNull(workflowVersion, "workflow version must not be null");
        Assert.notNull(workflowVersion.getWorkflowId(), "workflow id was null");
        Assert.hasText(workflowVersion.getVersion(), "version must not be empty");
        Assert.hasText(workflowVersion.getVersionData(), "version data must not be empty");
    }

    private void isValidated(WorkflowExecLogDTO workflowExecLog) {
        Assert.notNull(workflowExecLog, "workflow exec log must not be null");
        Assert.notNull(workflowExecLog.getWorkflowVersionId(), "workflow version id was null");
        Assert.hasText(workflowExecLog.getInputData(), "input data must not be empty");
        Assert.hasText(workflowExecLog.getOutputData(), "output data must not be empty");
        Assert.hasText(workflowExecLog.getExecData(), "exec data must not be empty");
        Assert.hasText(workflowExecLog.getExtraData(), "extra data must not be empty");
    }
}
