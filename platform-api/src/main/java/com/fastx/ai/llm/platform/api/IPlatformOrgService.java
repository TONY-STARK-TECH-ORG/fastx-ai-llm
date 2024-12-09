package com.fastx.ai.llm.platform.api;

import com.fastx.ai.llm.platform.dto.OrgDTO;

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

}
