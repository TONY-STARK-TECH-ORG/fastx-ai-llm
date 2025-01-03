package com.fastx.ai.llm.domains.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.dto.UserDTO;
import com.fastx.ai.llm.domains.entity.Organization;
import com.fastx.ai.llm.domains.entity.OrganizationUser;
import com.fastx.ai.llm.domains.entity.User;
import com.fastx.ai.llm.domains.service.IOrganizationService;
import com.fastx.ai.llm.domains.service.IOrganizationUserService;
import com.fastx.ai.llm.domains.service.IUserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
@DubboService
public class DubboUserServiceImpl extends DubboBaseDomainService implements IDubboUserService {

    @Autowired
    IUserService userService;

    @Autowired
    IOrganizationService organizationService;

    @Autowired
    IOrganizationUserService organizationUserService;

    @Override
    @SentinelResource("user.create")
    @Transactional(rollbackFor = Exception.class)
    public UserDTO createUser(String type, UserDTO userDTO) {
        isValidated(userDTO);
        if (IConstant.LOGIN_AUTH.equals(type)) {
            AssertUtil.assertNotBlank(userDTO.getAuthProvider(), "auth provider not found!");
            AssertUtil.assertNotBlank(userDTO.getAuthOpenId(), "auth openId not found!");
        } else {
            AssertUtil.assertNotBlank(userDTO.getEmail(), "email not found!");
            AssertUtil.assertNotBlank(userDTO.getPassword(), "password not found!");
        }

        userDTO.setRole(IConstant.NORMAL);
        userDTO.setStatus(IConstant.WAIT);

        User user = User.of(userDTO);
        AssertUtil.isTrue(userService.save(user), "save user failed!");
        // create same organization for user
        Organization organization = new Organization();
        organization.setName(user.getEmail());
        AssertUtil.isTrue(organizationService.save(organization), "save organization failed!");
        // create organization : user map
        OrganizationUser organizationUser = new OrganizationUser();
        organizationUser.setUserId(user.getId());
        organizationUser.setOrganizationId(organization.getId());
        AssertUtil.isTrue(organizationUserService.save(organizationUser), "save organization user failed!");
        // create user success
        return user.to();
    }

    @Override
    @SentinelResource("user.login")
    public UserDTO login(String email, String password) {
        return userService.login(email, password).to();
    }

    @Override
    @SentinelResource("user.login.auth")
    public UserDTO loginWithOAuth(String authProvider, String authOpenId) {
        return userService.loginWithOAuth(authProvider, authOpenId).to();
    }

    @Override
    @SentinelResource("user.login.info")
    public UserDTO loadUserByEmail(String email) {
        User user = userService.loadUserByEmail(email);
        Assert.notNull(user, "can't find user with email!");
        return user.to();
    }

    @Override
    @SentinelResource("user.login.info")
    public UserDTO loadUserByUserId(Long userId) {
        User user = userService.getById(userId);
        Assert.notNull(user, "can't find user with email!");
        user.setPassword("");
        return user.to();
    }

    private void isValidated(UserDTO userDTO) {
        AssertUtil.notNull(userDTO, "userDTO is null");
    }

}
