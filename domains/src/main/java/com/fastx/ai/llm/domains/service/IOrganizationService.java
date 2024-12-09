package com.fastx.ai.llm.domains.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fastx.ai.llm.domains.entity.Organization;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IOrganizationService extends IService<Organization> {

    /**
     * find all org by user id
     * @param userId
     * @return org list or empty
     */
    List<Organization> findByUserId(Long userId);

}
