package com.fastx.ai.llm.platform.api;

import com.fastx.ai.llm.platform.dto.OrgDTO;
import com.fastx.ai.llm.platform.dto.OrgToolDTO;

import java.util.List;

/**
 * @author stark
 */
public interface IPlatformOrgService {

    /**
     * get org by orgId
     * @param orgId
     * @return single org
     */
    OrgDTO getOrgById(Long orgId);

    /**
     * get org list by user id
     * @param userId
     * @return list
     */
    List<OrgDTO> getOrgByUserId(Long userId);

    /**
     * create org tool
     * @param orgToolDTO info
     * @return create result
     */
    OrgToolDTO createOrgTools(OrgToolDTO orgToolDTO);

    /**
     * update org tool
     * @param orgToolsDTO with id update
     * @return true or false
     */
    boolean updateOrgTools(OrgToolDTO orgToolsDTO);

    /**
     * delete a tool org tool config
     * @param id id
     * @return result
     */
    boolean deleteOrgTools(Long id);

    /**
     * list all tools in organization
     * @param orgId org id
     * @return tool list
     */
    List<OrgToolDTO> getOrgTools(Long orgId);
}
