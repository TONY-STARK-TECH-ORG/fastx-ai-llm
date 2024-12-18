package com.fastx.ai.llm.domains.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.dto.OrganizationDTO;
import com.fastx.ai.llm.domains.entity.Organization;
import com.fastx.ai.llm.domains.service.IOrganizationService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
@DubboService
public class DubboOrganizationServiceImpl extends DubboBaseDomainService implements IDubboOrganizationService {

    @Autowired
    IOrganizationService organizationService;

    @Override
    @SentinelResource("org.find")
    public OrganizationDTO findByOrganizationId(Long organizationId) {
        Organization organization = organizationService.getById(organizationId);
        Assert.notNull(organization, "organization not found!");
        return organization.to();
    }

    @Override
    @SentinelResource("org.findBy.user")
    public List<OrganizationDTO> findByUserId(Long userId) {
        return organizationService.findByUserId(userId)
                .stream().map(Organization::to).toList();
    }

}
