package com.fastx.ai.llm.domains.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.dto.OrganizationToolsDTO;
import com.fastx.ai.llm.domains.entity.Organization;
import com.fastx.ai.llm.domains.entity.OrganizationTools;
import com.fastx.ai.llm.domains.service.IOrganizationService;
import com.fastx.ai.llm.domains.service.IOrganizationToolsService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author stark
 */
@DubboService
public class DubboToolServiceImpl implements IDubboToolService {

    @Autowired
    IOrganizationToolsService organizationToolsService;

    @Autowired
    IOrganizationService organizationService;

    @Override
    @SentinelResource("org.tool.create")
    public OrganizationToolsDTO createOrganizationTools(OrganizationToolsDTO organizationToolsDTO) {
        isValidated(organizationToolsDTO);
        OrganizationTools organizationTools = OrganizationTools.of(organizationToolsDTO);
        // query
        OrganizationTools tools = organizationToolsService.getByOrganizationIdAndCodeVersion(
                organizationTools.getOrganizationId(),
                organizationTools.getToolCode(),
                organizationTools.getToolVersion()
        );
        if (tools != null) {
            // already has same tools
            tools.setConfigData(organizationTools.getConfigData());
            tools.setCustom(organizationTools.getCustom());
            tools.setCustomCode(organizationTools.getCustomCode());
            Assert.isTrue(organizationToolsService.updateById(tools), "update organizationTools failed");
            return tools.to();
        } else {
            // create new
            Assert.isTrue(organizationToolsService.save(organizationTools), "save organizationTools failed");
        }
        return organizationTools.to();
    }

    @Override
    @SentinelResource("org.tool.update")
    public boolean updateOrganizationTools(OrganizationToolsDTO organizationToolsDTO) {
        isValidated(organizationToolsDTO);
        Assert.notNull(organizationToolsDTO.getId(), "organizationId must not be null");
        OrganizationTools organizationTools = OrganizationTools.of(organizationToolsDTO);
        return organizationToolsService.updateById(organizationTools);
    }

    @Override
    @SentinelResource("org.tool.delete")
    public boolean deleteOrganizationTools(Long id) {
        Assert.notNull(id, "organizationId must not be null");
        return organizationToolsService.removeById(id);
    }

    @Override
    @SentinelResource("org.tool.get")
    public List<OrganizationToolsDTO> getTools(Long userId) {
        Assert.notNull(userId, "userId must not be null");
        List<Organization> organizationList = organizationService.findByUserId(userId);
        List<OrganizationTools> organizationTools =
                organizationToolsService.getOrganizationToolsByOrganizationIds(
                        organizationList.stream().map(Organization::getId).collect(Collectors.toList())
                );
        return organizationTools.stream().map(OrganizationTools::to).collect(Collectors.toList());
    }

    private void isValidated(OrganizationToolsDTO organizationTools) {
        Assert.notNull(organizationTools, "organizationTools is null");
        Assert.notNull(organizationTools.getOrganizationId(), "organizationId is null");
        Assert.hasText(organizationTools.getToolCode(), "toolCode is null");
        Assert.hasText(organizationTools.getToolVersion(), "toolVersion is null");
        Assert.hasText(organizationTools.getConfigData(), "configData is null");
    }
}
