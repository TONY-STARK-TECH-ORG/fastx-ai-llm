package com.fastx.ai.llm.web.controller;

import com.fastx.ai.llm.platform.api.IPlatformOrgService;
import com.fastx.ai.llm.platform.api.IPlatformUserService;
import com.fastx.ai.llm.platform.dto.OrgDTO;
import com.fastx.ai.llm.platform.dto.UserInfoDTO;
import com.fastx.ai.llm.web.controller.entity.Response;
import com.fastx.ai.llm.web.service.JwtService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author stark
 */
@RestController
@RequestMapping("/auth/user")
public class FastLlmUserController {

    @DubboReference
    IPlatformUserService platformUserService;

    @DubboReference
    IPlatformOrgService platformOrgService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/createWithEmail")
    public Response<UserInfoDTO> createWithEmail(@RequestBody UserInfoDTO userInfoDTO) {
        UserInfoDTO user = platformUserService.createUser("email", userInfoDTO);
        Assert.notNull(user, "sign in failed");
        user.setToken(jwtService.generateToken(user.getEmail()));
        return Response.success(user);
    }

    @PostMapping("/createWithAuth")
    public Response<UserInfoDTO> createWithAuth(@RequestBody UserInfoDTO userInfoDTO) {
        UserInfoDTO user = platformUserService.createUser("auth", userInfoDTO);
        Assert.notNull(user, "sign in failed");
        user.setToken(jwtService.generateToken(user.getEmail()));
        return Response.success(user);
    }

    @PostMapping("/loginWithEmail")
    public Response<UserInfoDTO> loginWithEmail(@RequestBody UserInfoDTO userInfoDTO) {
        UserInfoDTO user = platformUserService.login(userInfoDTO.getEmail(), userInfoDTO.getPassword());
        Assert.notNull(user, "sign in failed");
        user.setToken(jwtService.generateToken(user.getEmail()));
        return Response.success(user);
    }

    @PostMapping("/loginWitAuth")
    public Response<UserInfoDTO> loginWitAuth(@RequestBody UserInfoDTO userInfoDTO) {
        UserInfoDTO user = platformUserService.loginByAuth(userInfoDTO.getAuthProvider(), userInfoDTO.getAuthOpenId());
        Assert.notNull(user, "sign in failed");
        user.setToken(jwtService.generateToken(user.getEmail()));
        return Response.success(user);
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
