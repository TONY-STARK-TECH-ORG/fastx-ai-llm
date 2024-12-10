package com.fastx.ai.llm.web.controller;

import com.fastx.ai.llm.platform.api.IPlatformOrgService;
import com.fastx.ai.llm.platform.dto.OrgTaskDTO;
import com.fastx.ai.llm.platform.dto.OrgTaskLogDTO;
import com.fastx.ai.llm.platform.dto.PlatformPagaDTO;
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

    @PostMapping("org/task/log/create")
    public Response<OrgTaskLogDTO> update(@RequestBody OrgTaskLogDTO orgTaskLogDTO) {
        return Response.success(platformOrgService.createTaskLog(orgTaskLogDTO));
    }

    @GetMapping("org/task/log/list")
    public Response<PlatformPagaDTO<OrgTaskLogDTO>> listTaskLogs(Long taskId, Long page, Long size, String status) {
        return Response.success(
                platformOrgService.getTaskLogsByTaskId(taskId, page, size, status)
        );
    }

}
