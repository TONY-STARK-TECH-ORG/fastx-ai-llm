package com.fastx.ai.llm.platform.service.organization;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.api.IDubboOrganizationService;
import com.fastx.ai.llm.domains.dto.OrganizationDTO;
import com.fastx.ai.llm.platform.api.IPlatformOrgService;
import com.fastx.ai.llm.platform.dto.OrgDTO;
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

}
