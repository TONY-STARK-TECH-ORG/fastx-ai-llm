package com.fastx.ai.llm.platform.api;

import com.fastx.ai.llm.platform.dto.UserInfoDTO;

/**
 * @author stark
 */
public interface IPlatformUserService {

    /**
     * create a new user
     * will sync create organization and organization-user
     * @param user user info
     * @return created user with user id
     */
    UserInfoDTO createUser(String type, UserInfoDTO user);

    /**
     * login
     * @param email email address
     * @param password pwd md5
     * @return user or exception
     */
    UserInfoDTO login(String email, String password);

    /**
     * login by auth
     * @param authProvider google ...
     * @param authOpenId openId
     * @return user or exception
     */
    UserInfoDTO loginByAuth(String authProvider, String authOpenId);

    /**
     * load userinfo by email
     * @param email username (email)
     * @return user info
     */
    UserInfoDTO loadUserByEmail(String email);

    /**
     * get user info by user id
     * @param userId user id
     * @return user info
     */
    UserInfoDTO getUserinfo(Long userId);
}
