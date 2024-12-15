package com.fastx.ai.llm.web.controller;

import com.fastx.ai.llm.platform.api.IPlatformAppService;
import com.fastx.ai.llm.platform.dto.AppDTO;
import com.fastx.ai.llm.platform.dto.AppVersionDTO;
import com.fastx.ai.llm.web.controller.entity.Response;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author stark
 */
@RestController
@RequestMapping("/app")
public class FastLlmAppController {

    @DubboReference
    IPlatformAppService platformAppService;

    @PostMapping("/create")
    public Response<AppDTO> create(@RequestBody AppDTO appDTO) {
        // create application
        return Response.success(platformAppService.createApp(appDTO));
    }

    @PostMapping("/update")
    public Response<Boolean> update(@RequestBody AppDTO appDTO) {
        return Response.success(platformAppService.updateApp(appDTO));
    }

    @GetMapping("/list")
    public Response<List<AppDTO>> listApp(Long orgId) {
        return Response.success(
                platformAppService.getAppList(orgId)
        );
    }

    @PostMapping("/version/create")
    public Response<AppVersionDTO> createVersion(@RequestBody AppVersionDTO appVersionDTO) {
        return Response.success(
                platformAppService.createNewAppVersion(appVersionDTO)
        );
    }

    @PostMapping("/version/update")
    public Response<Boolean> updateVersion(@RequestBody AppVersionDTO appVersionDTO) {
        return Response.success(
                platformAppService.updateAppVersion(appVersionDTO)
        );
    }

    @GetMapping("/version/list")
    public Response<List<AppVersionDTO>> listVersion(Long appId) {
        return Response.success(
                platformAppService.getAppVersionList(appId)
        );
    }

    @PostMapping("/version/active")
    public Response<Boolean> activeVersion(@RequestBody AppVersionDTO appVersionDTO) {
        return Response.success(
                platformAppService.activateAppVersion(appVersionDTO)
        );
    }

    @PostMapping("/version/inactive")
    public Response<Boolean> inactiveVersion(@RequestBody AppVersionDTO appVersionDTO) {
        return Response.success(
                platformAppService.inactivateAppVersion(appVersionDTO)
        );
    }

    @PostMapping("delete")
    public Response<Boolean> delete(@RequestBody AppDTO appDTO) {
        return Response.success(
                platformAppService.deleteApp(appDTO.getId())
        );
    }

    @PostMapping("/version/delete")
    public Response<Boolean> deleteVersion(@RequestBody AppVersionDTO appVersionDTO) {
        return Response.success(
                platformAppService.deleteAppVersion(appVersionDTO.getId())
        );
    }

}
