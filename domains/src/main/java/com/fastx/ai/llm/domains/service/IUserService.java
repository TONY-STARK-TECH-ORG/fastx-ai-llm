package com.fastx.ai.llm.domains.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fastx.ai.llm.domains.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IUserService extends IService<User> {

    /**
     * login with email and md5 password
     * @param email
     * @param password md5 password
     * @return user or exception
     */
    User login(String email, String password);

    /**
     * login with auth
     * @param authProvider google or ...
     * @param authOpenId openId
     * @return user or exception
     */
    User loginWithOAuth(String authProvider, String authOpenId);
}
