package com.fastx.ai.llm.web.controller;

import com.fastx.ai.llm.platform.api.IPlatformOrgService;
import com.fastx.ai.llm.platform.api.IPlatformUserService;
import com.fastx.ai.llm.platform.dto.OrgDTO;
import com.fastx.ai.llm.platform.dto.UserInfoDTO;
import com.fastx.ai.llm.web.controller.entity.Response;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author stark
 */
@RestController
@RequestMapping("/user")
public class FastLlmUserController {

    @DubboReference
    IPlatformUserService platformUserService;

    @DubboReference
    IPlatformOrgService platformOrgService;

    @PostMapping("/createWithEmail")
    public Response<UserInfoDTO> createWithEmail(@RequestBody UserInfoDTO userInfoDTO) {
        return Response.success(platformUserService.createUser("email", userInfoDTO));
    }

    @PostMapping("/createWithAuth")
    public Response<UserInfoDTO> createWithAuth(@RequestBody UserInfoDTO userInfoDTO) {
        return Response.success(platformUserService.createUser("auth", userInfoDTO));
    }

    @PostMapping("'/loginWithEmail")
    public Response<UserInfoDTO> loginWithEmail(UserInfoDTO userInfoDTO) {
        return Response.success(
                platformUserService.login(userInfoDTO.getEmail(), userInfoDTO.getPassword()));
    }

    @PostMapping("'/loginWitAuth")
    public Response<UserInfoDTO> loginWitAuth(UserInfoDTO userInfoDTO) {
        return Response.success(
                platformUserService.loginByAuth(userInfoDTO.getAuthProvider(), userInfoDTO.getAuthOpenId()));
    }

    @GetMapping("getOrganizationsByUserId")
    public Response<List<OrgDTO>> getOrganizationsByUserId(Long userId) {
        return Response.success(
                platformOrgService.getOrgByUserId(userId)
        );
    }

    @GetMapping("getOrganization")
    public Response<OrgDTO> getOrganization(Long orgId) {
        return Response.success(
                platformOrgService.getOrgById(orgId)
        );
    }

}
