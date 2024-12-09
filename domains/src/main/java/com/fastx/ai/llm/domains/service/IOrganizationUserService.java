package com.fastx.ai.llm.domains.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fastx.ai.llm.domains.entity.OrganizationUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IOrganizationUserService extends IService<OrganizationUser> {

    /**
     * find org user was joined
     * @param userId user Id
     * @return user joined org
     */
    List<OrganizationUser> findByUserId(Long userId);

}
