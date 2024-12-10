package com.fastx.ai.llm.platform.service.organization;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.api.IDubboOrganizationService;
import com.fastx.ai.llm.domains.api.IDubboToolService;
import com.fastx.ai.llm.domains.dto.OrganizationDTO;
import com.fastx.ai.llm.domains.dto.OrganizationToolsDTO;
import com.fastx.ai.llm.platform.api.IPlatformOrgService;
import com.fastx.ai.llm.platform.dto.OrgDTO;
import com.fastx.ai.llm.platform.dto.OrgToolDTO;
import com.rometools.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author stark
 */
@DubboService
public class PlatformOrgServiceImpl implements IPlatformOrgService {

    @DubboReference
    IDubboOrganizationService organizationService;

    @DubboReference
    IDubboToolService toolService;

    @Override
    @SentinelResource("org.getBy.id")
    public OrgDTO getOrgById(Long orgId) {
        OrganizationDTO organization = organizationService.findByOrganizationId(orgId);
        Assert.notNull(organization, "organization not found");
        OrgDTO org = new OrgDTO();
        BeanUtils.copyProperties(organization, org);
        return org;
    }

    @Override
    @SentinelResource("org.getBy.userId")
    public List<OrgDTO> getOrgByUserId(Long userId) {
        List<OrganizationDTO> organizationList = organizationService.findByUserId(userId);
        Assert.notEmpty(organizationList, "organization not found");
        return organizationList.stream().map(o -> {
            OrgDTO org = new OrgDTO();
            BeanUtils.copyProperties(o, org);
            return org;
        }).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("org.tool.create")
    public OrgToolDTO createOrgTools(OrgToolDTO orgToolDTO) {
        Assert.notNull(orgToolDTO, "orgTool is null");
        OrganizationToolsDTO organizationToolsDTO = new OrganizationToolsDTO();
        // create
        BeanUtils.copyProperties(orgToolDTO, organizationToolsDTO);
        OrganizationToolsDTO organizationTools =
                toolService.createOrganizationTools(organizationToolsDTO);
        Assert.notNull(organizationTools, "create failed");
        // create dto
        OrgToolDTO dto = new OrgToolDTO();
        BeanUtils.copyProperties(organizationTools, dto);
        return dto;
    }

    @Override
    @SentinelResource("org.tool.update")
    public boolean updateOrgTools(OrgToolDTO orgToolsDTO) {
        Assert.notNull(orgToolsDTO, "orgTools is null");
        Assert.notNull(orgToolsDTO.getId(), "orgTools id is null");
        OrganizationToolsDTO organizationToolsDTO = new OrganizationToolsDTO();
        BeanUtils.copyProperties(orgToolsDTO, organizationToolsDTO);
        return toolService.updateOrganizationTools(organizationToolsDTO);
    }

    @Override
    @SentinelResource("org.tool.delete")
    public boolean deleteOrgTools(Long id) {
        Assert.notNull(id, "id is null");
        return toolService.deleteOrganizationTools(id);
    }

    @Override
    @SentinelResource("org.tool.get")
    public List<OrgToolDTO> getOrgTools(Long orgId) {
        Assert.notNull(orgId, "orgId is null");
        List<OrganizationToolsDTO> organizationTools = Lists.emptyToNull(
                toolService.getOrganizationTools(orgId));
        return organizationTools.stream().map(ogt -> {
            OrgToolDTO orgTool = new OrgToolDTO();
            BeanUtils.copyProperties(ogt, orgTool);
            return orgTool;
        }).collect(Collectors.toList());
    }

}
