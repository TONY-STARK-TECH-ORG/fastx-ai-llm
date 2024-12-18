package com.fastx.ai.llm.web.controller;

import com.fastx.ai.llm.platform.api.IPlatformOrgService;
import com.fastx.ai.llm.platform.dto.OrgTaskDTO;
import com.fastx.ai.llm.platform.dto.OrgTaskExecDTO;
import com.fastx.ai.llm.platform.dto.OrgTaskNodeExecDTO;
import com.fastx.ai.llm.platform.dto.PlatformPageDTO;
import com.fastx.ai.llm.web.controller.entity.Response;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author stark
 */
@RestController
@RequestMapping("/task")
public class FastLlmTaskController {

    @DubboReference
    IPlatformOrgService platformOrgService;

    @PostMapping("/org/task/create")
    public Response<OrgTaskDTO> create(@RequestBody OrgTaskDTO orgTaskDTO) {
        return Response.success(platformOrgService.createTask(orgTaskDTO));
    }

    @PostMapping("org/task/update")
    public Response<Boolean> update(@RequestBody OrgTaskDTO orgTaskDTO) {
        return Response.success(platformOrgService.updateTask(orgTaskDTO));
    }

    @GetMapping("org/task/list")
    public Response<List<OrgTaskDTO>> listTasks(Long orgId) {
        return Response.success(
                platformOrgService.getTasksByOrgId(orgId)
        );
    }

    @PostMapping("org/task/delete")
    public Response<Boolean> delete(@RequestBody OrgTaskDTO orgTaskDTO) {
        return Response.success(
                platformOrgService.deleteTask(orgTaskDTO.getId())
        );
    }

    @PostMapping("org/task/exec/create")
    public Response<OrgTaskExecDTO> createTaskExec(@RequestBody OrgTaskExecDTO orgTaskExecDTO) {
        return Response.success(platformOrgService.createTaskExec(orgTaskExecDTO));
    }

    @GetMapping("org/task/exec/list")
    public Response<PlatformPageDTO<OrgTaskExecDTO>> listTaskExecs(Long taskId, Long page, Long size, String status, String type) {
        return Response.success(
                platformOrgService.getTaskExecsByTaskId(taskId, page, size, status, type)
        );
    }

    @GetMapping("org/task/exec/node/list")
    public Response<List<OrgTaskNodeExecDTO>> listTaskNodeExecs(Long taskExecId) {
        return Response.success(
                platformOrgService.getTaskNodeExecs(taskExecId)
        );
    }

    @GetMapping("org/task/exec/node/list")
    public Response<PlatformPageDTO<OrgTaskNodeExecDTO>> listTaskNodeExecsByPage(Long page, Long size, String status, Boolean checkPrevNodes) {
        return Response.success(
                platformOrgService.getTaskNodeExecs(page, size, status, checkPrevNodes)
        );
    }

}
