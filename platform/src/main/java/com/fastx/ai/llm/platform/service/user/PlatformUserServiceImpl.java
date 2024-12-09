package com.fastx.ai.llm.platform.service.user;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.api.IDubboUserService;
import com.fastx.ai.llm.domains.dto.UserDTO;
import com.fastx.ai.llm.platform.api.IPlatformUserService;
import com.fastx.ai.llm.platform.dto.UserInfoDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/**
 * @author stark
 */
@DubboService
public class PlatformUserServiceImpl implements IPlatformUserService {

    @DubboReference
    IDubboUserService userService;

    @Override
    @SentinelResource("user.create")
    public UserInfoDTO createUser(String type, UserInfoDTO user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        // create user.
        UserDTO createdUser = userService.createUser(type, userDTO);
        Assert.notNull(createdUser, "user create failed");

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(createdUser, userInfoDTO);
        return userInfoDTO;
    }

    @Override
    @SentinelResource("user.login")
    public UserInfoDTO login(String email, String password) {
        UserDTO user = userService.login(email, password);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfoDTO);
        return userInfoDTO;
    }

    @Override
    @SentinelResource("user.login.auth")
    public UserInfoDTO loginByAuth(String authProvider, String authOpenId) {
        UserDTO user = userService.login(authProvider, authOpenId);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfoDTO);
        return userInfoDTO;
    }

}
