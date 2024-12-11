package com.fastx.ai.llm.web.controller;

import com.fastx.ai.llm.platform.api.IPlatformOrgService;
import com.fastx.ai.llm.platform.api.IPlatformToolService;
import com.fastx.ai.llm.platform.dto.OrgToolDTO;
import com.fastx.ai.llm.platform.dto.ToolDTO;
import com.fastx.ai.llm.web.controller.entity.Response;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("platform/tool/exec")
    public Response<Map<String, Object>> execTool(@RequestBody Map<String, Object> params) {
        String toolCode = (String) params.get("toolCode");
        String toolVersion = (String) params.get("toolVersion");
        String type = (String) params.get("type");
        Map<String, Object> input = (Map<String, Object>) params.get("input");
        return Response.success(
                platformToolService.execTool(toolCode, toolVersion, type, input)
        );
    }

}
