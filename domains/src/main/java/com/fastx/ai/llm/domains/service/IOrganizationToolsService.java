package com.fastx.ai.llm.domains.service;

import com.fastx.ai.llm.domains.entity.OrganizationTools;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IOrganizationToolsService extends IService<OrganizationTools> {

    /**
     * get all tools under org
     * @param organizationIds org id
     * @return all tools
     */
    List<OrganizationTools> getOrganizationToolsByOrganizationIds(List<Long> organizationIds);
}
