package com.fastx.ai.llm.web.controller;

import com.fastx.ai.llm.platform.api.IPlatformOrgService;
import com.fastx.ai.llm.platform.api.IPlatformToolService;
import com.fastx.ai.llm.platform.dto.OrgToolDTO;
import com.fastx.ai.llm.platform.dto.ToolDTO;
import com.fastx.ai.llm.web.controller.entity.Response;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author stark
 */
@RestController
@RequestMapping("/tool")
public class FastLlmToolController {

    @DubboReference
    IPlatformOrgService platformOrgService;

    @DubboReference
    IPlatformToolService platformToolService;

    @PostMapping("/org/tool/create")
    public Response<OrgToolDTO> create(@RequestBody OrgToolDTO orgToolDTO) {
        return Response.success(platformOrgService.createOrgTools(orgToolDTO));
    }

    @PostMapping("org/tool/update")
    public Response<Boolean> update(@RequestBody OrgToolDTO orgToolDTO) {
        return Response.success(platformOrgService.updateOrgTools(orgToolDTO));
    }

    @GetMapping("org/tool/list")
    public Response<List<OrgToolDTO>> listApp(Long orgId) {
        return Response.success(
                platformOrgService.getOrgTools(orgId)
        );
    }

    @PostMapping("org/tool/delete")
    public Response<Boolean> delete(@RequestBody OrgToolDTO orgToolDTO) {
        return Response.success(
                platformOrgService.deleteOrgTools(orgToolDTO.getId())
        );
    }

    @GetMapping("/platform/tool/list")
    public Response<List<ToolDTO>> list() {
        return Response.success(
                platformToolService.getPlatformTools()
        );
    }

}
