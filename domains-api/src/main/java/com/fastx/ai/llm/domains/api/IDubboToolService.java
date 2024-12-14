package com.fastx.ai.llm.domains.api;

import com.fastx.ai.llm.domains.dto.OrganizationToolsDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IDubboToolService {

    /**
     * create org tool
     * @param organizationToolsDTO info
     * @return create result
     */
    OrganizationToolsDTO createOrganizationTools(OrganizationToolsDTO organizationToolsDTO);

    /**
     * update org tool
     * @param organizationToolsDTO with id update
     * @return true or false
     */
    boolean updateOrganizationTools(OrganizationToolsDTO organizationToolsDTO);

    /**
     * delete a tool org tool config
     * @param id id
     * @return result
     */
    boolean deleteOrganizationTools(Long id);

    /**
     * list all tools under user (all orgs user joined)
     * @param userId user id
     * @return tool list
     */
    List<OrganizationToolsDTO> getTools(Long userId);

}
