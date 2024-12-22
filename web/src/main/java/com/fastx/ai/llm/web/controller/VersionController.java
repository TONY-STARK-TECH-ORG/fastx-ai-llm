package com.fastx.ai.llm.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.fastx.ai.llm.platform.api.IPlatformBasicInfoService;
import com.fastx.ai.llm.web.controller.entity.Response;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author stark
 */
@RestController
@RequestMapping("/info")
public class VersionController {

    @DubboReference
    IPlatformBasicInfoService basicInfoService;

    @SaIgnore
    @GetMapping("/version")
    public Response<String> info() {
        return Response.success(
                "app: 0.0.1, " + basicInfoService.getBasicInfo()
        );
    }

}
