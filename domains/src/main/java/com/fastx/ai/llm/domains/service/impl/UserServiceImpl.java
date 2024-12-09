package com.fastx.ai.llm.domains.service.impl;

import com.alibaba.csp.sentinel.util.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.entity.User;
import com.fastx.ai.llm.domains.mapper.UserMapper;
import com.fastx.ai.llm.domains.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User login(String email, String password) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getEmail, email);
        wrapper.eq(User::getPassword, password);
        wrapper.in(User::getStatus, IConstant.ACTIVE, IConstant.WAIT);
        User user = this.getOne(wrapper);
        AssertUtil.notNull(user, "can't find user with email and password!");
        return user;
    }

    @Override
    public User loginWithOAuth(String authProvider, String authOpenId) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getAuthOpenId, authOpenId);
        wrapper.eq(User::getAuthProvider, authProvider);
        wrapper.eq(User::getStatus, IConstant.ACTIVE);
        User user = this.getOne(wrapper);
        AssertUtil.notNull(user, "can't find user with email and password!");
        return user;
    }
}
