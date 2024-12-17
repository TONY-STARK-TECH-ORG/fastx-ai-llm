package com.fastx.ai.llm.web.controller;

import com.fastx.ai.llm.platform.api.IPlatformOrgService;
import com.fastx.ai.llm.platform.dto.OrgWorkflowDTO;
import com.fastx.ai.llm.platform.dto.OrgWorkflowExecLogDTO;
import com.fastx.ai.llm.platform.dto.OrgWorkflowVersionDTO;
import com.fastx.ai.llm.web.controller.entity.Response;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author stark
 */
@RestController
@RequestMapping("/workflow")
public class FastLlmWorkflowController {

    @DubboReference
    IPlatformOrgService platformOrgService;

    @PostMapping("/org/workflow/create")
    public Response<OrgWorkflowDTO> create(@RequestBody OrgWorkflowDTO orgWorkflowDTO) {
        return Response.success(platformOrgService.createWorkflow(orgWorkflowDTO));
    }

    @PostMapping("org/workflow/update")
    public Response<Boolean> update(@RequestBody OrgWorkflowDTO orgWorkflowDTO) {
        return Response.success(platformOrgService.updateWorkflow(orgWorkflowDTO));
    }

    @GetMapping("org/workflow/list")
    public Response<List<OrgWorkflowDTO>> listWorkflows(Long orgId) {
        return Response.success(
                platformOrgService.getWorkflowsByOrgId(orgId)
        );
    }

    @PostMapping("org/workflow/delete")
    public Response<Boolean> delete(@RequestBody OrgWorkflowDTO orgWorkflowDTO) {
        return Response.success(
                platformOrgService.deleteWorkflow(orgWorkflowDTO.getId())
        );
    }

    @PostMapping("org/workflow/version/create")
    public Response<OrgWorkflowVersionDTO> createWorkflowVersion(@RequestBody OrgWorkflowVersionDTO orgWorkflowVersionDTO) {
        return Response.success(platformOrgService.createWorkflowVersion(orgWorkflowVersionDTO));
    }

    @PostMapping("org/workflow/version/update")
    public Response<Boolean> updateWorkflowVersion(@RequestBody OrgWorkflowVersionDTO orgWorkflowVersionDTO) {
        return Response.success(platformOrgService.updateWorkflowVersion(orgWorkflowVersionDTO));
    }

    @GetMapping("org/workflow/version/list")
    public Response<List<OrgWorkflowVersionDTO>> listWorkflowVersions(Long workflowId) {
        return Response.success(platformOrgService.getWorkflowVersionsByWorkflowId(workflowId));
    }

    @PostMapping("org/workflow/version/delete")
    public Response<Boolean> deleteWorkflowVersion(@RequestBody OrgWorkflowVersionDTO orgWorkflowVersionDTO) {
        return Response.success(platformOrgService.deleteWorkflowVersion(orgWorkflowVersionDTO.getId()));
    }

    @PostMapping("org/workflow/version/exec")
    public Response<Map<String, Object>> executeWorkflowVersion(@RequestBody OrgWorkflowVersionDTO orgWorkflowVersionDTO) {
        return Response.success(platformOrgService.executeWorkflowVersion(orgWorkflowVersionDTO.getId()));
    }

    @GetMapping("org/workflow/version/get")
    public Response<OrgWorkflowVersionDTO> getWorkflowVersion(Long orgWorkflowVersionId) {
        return Response.success(platformOrgService.getWorkflowVersion(orgWorkflowVersionId));
    }

    @GetMapping("org/workflow/exec/log/list")
    public Response<List<OrgWorkflowExecLogDTO>> listWorkflowExecLogs(Long workflowVersionId, Long page, Long size) {
        return Response.success(
                platformOrgService.getWorkflowExecLogsByWorkflowVersionId(workflowVersionId, page, size).getList());
    }

    @GetMapping("org/workflow/exec/log/get")
    public Response<OrgWorkflowExecLogDTO> getWorkflowExecLog(Long orgWorkflowExecLogId) {
        return Response.success(platformOrgService.getWorkflowExecLog(orgWorkflowExecLogId));
    }

    @PostMapping("org/workflow/exec/log/create")
    public Response<OrgWorkflowExecLogDTO> createWorkflowExecLog(@RequestBody OrgWorkflowExecLogDTO orgWorkflowExecLogDTO) {
        return Response.success(platformOrgService.createWorkflowExecLog(orgWorkflowExecLogDTO));
    }

}
