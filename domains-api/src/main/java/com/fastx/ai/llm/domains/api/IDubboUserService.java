package com.fastx.ai.llm.domains.api;

import com.fastx.ai.llm.domains.dto.UserDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IDubboUserService {

    /**
     * create a new user
     * @param type login type: email, auth
     * @param userDTO user info
     * @return created user
     */
    UserDTO createUser(String type, UserDTO userDTO);

    /**
     * login with email and password
     * @param email
     * @param password
     * @return user or exception
     */
    UserDTO login(String email, String password);

    /**
     * login with auth method
     * @param authProvider
     * @param authOpenId
     * @return user or exception
     */
    UserDTO loginWithOAuth(String authProvider, String authOpenId);

}
