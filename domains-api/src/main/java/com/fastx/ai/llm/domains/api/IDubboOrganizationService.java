package com.fastx.ai.llm.domains.api;

import com.fastx.ai.llm.domains.dto.OrganizationDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IDubboOrganizationService {

    /**
     * find organization by id
     * @param organizationId id
     * @return org dto
     */
    OrganizationDTO findByOrganizationId(Long organizationId);

    /**
     * find all org user join
     * @param userId
     * @return org list
     */
    List<OrganizationDTO> findByUserId(Long userId);

}
