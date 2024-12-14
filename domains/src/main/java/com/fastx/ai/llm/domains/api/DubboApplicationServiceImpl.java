package com.fastx.ai.llm.domains.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.fastx.ai.llm.domains.dto.ApplicationDTO;
import com.fastx.ai.llm.domains.dto.ApplicationVersionDTO;
import com.fastx.ai.llm.domains.entity.Application;
import com.fastx.ai.llm.domains.entity.ApplicationVersion;
import com.fastx.ai.llm.domains.entity.OrganizationUser;
import com.fastx.ai.llm.domains.service.IApplicationService;
import com.fastx.ai.llm.domains.service.IApplicationVersionService;
import com.fastx.ai.llm.domains.service.IOrganizationUserService;
import com.rometools.utils.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
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
@Slf4j
@DubboService
public class DubboApplicationServiceImpl extends DubboBaseDomainService implements IDubboApplicationService {

    @Autowired
    IApplicationService applicationService;

    @Autowired
    IOrganizationUserService organizationUserService;

    @Autowired
    IApplicationVersionService applicationVersionService;

    @SentinelResource("application.create")
    @Override
    public ApplicationDTO createApplication(ApplicationDTO applicationDTO) {
        log.info("GLOBAL TX XID: {}", RootContext.getXID());
        isValidated(applicationDTO);
        // set application to inactive state
        applicationDTO.setStatus("inactive");
        // save to db
        Application application = Application.of(applicationDTO);
        AssertUtil.assertTrue(
                applicationService.save(application),
                "application save failed"
        );
        return application.to();
    }

    @SentinelResource("application.list")
    @Override
    public List<ApplicationDTO> getApplications(Long userId) {
        List<OrganizationUser> organizationUserList = organizationUserService.findByUserId(userId);
        if (CollectionUtils.isEmpty(organizationUserList)) {
            return new ArrayList<>();
        }
        // find all application under org
        return applicationService.findByOrgIds(
                // convert org to id
                organizationUserList.stream().map(OrganizationUser::getId).collect(Collectors.toList())
                ).stream().map(Application::to).collect(Collectors.toList());
    }

    @SentinelResource("application.update")
    @Override
    public boolean updateApplication(ApplicationDTO applicationDTO) {
        isValidated(applicationDTO);
        AssertUtil.notNull(applicationDTO.getId(), "application id is null");
        return applicationService.updateById(Application.of(applicationDTO));
    }

    @SentinelResource("application.delete")
    @Override
    public boolean deleteApplication(Long applicationId) {
        AssertUtil.notNull(applicationId, "application id is null");
        Assert.isTrue(applicationService.removeById(applicationId), "application delete failed");
        List<ApplicationVersionDTO> applicationVersions = getApplicationVersions(applicationId);
        if (CollectionUtils.isNotEmpty(applicationVersions)) {
            Assert.isTrue(
                    applicationVersionService.removeByIds(
                            Lists.createWhenNull(applicationVersions).stream().map(ApplicationVersionDTO::getId)
                                    .collect(Collectors.toList())),
                    "delete application version failed");
        }
        return true;
    }

    @SentinelResource("application.version.list")
    @Override
    public List<ApplicationVersionDTO> getApplicationVersions(Long applicationId) {
        return applicationVersionService.findByAppId(applicationId)
                .stream().map(ApplicationVersion::to).collect(Collectors.toList());
    }

    @SentinelResource("application.version.create")
    @Override
    public ApplicationVersionDTO createVersionData(Long applicationId, String versionData, String version) {
        return applicationVersionService.createVersion(applicationId, versionData, version).to();
    }

    @SentinelResource("application.version.update")
    @Override
    public boolean updateVersionData(Long applicationVersionId, String versionData) {
        return applicationVersionService.updateVersionData(applicationVersionId, versionData);
    }

    @SentinelResource("application.version.active")
    @Override
    public boolean activeVersion(Long applicationVersionId) {
        return applicationVersionService.activeVersion(applicationVersionId);
    }

    @SentinelResource("application.version.inactive")
    @Override
    public boolean inactiveVersion(Long applicationVersionId) {
        return applicationVersionService.inactiveVersion(applicationVersionId);
    }

    @SentinelResource("application.version.remove")
    @Override
    public boolean deleteVersion(Long applicationVersionId) {
        return applicationVersionService.removeById(applicationVersionId);
    }

    private void isValidated(ApplicationDTO application) {
        AssertUtil.notNull(application, "application is null");
        AssertUtil.assertNotBlank(application.getName(), "application name is null");
        AssertUtil.assertNotBlank(application.getDescription(), "application description is null");
        AssertUtil.assertTrue(
                UrlValidator.getInstance().isValid(application.getIconUrl()),
                "application icon url is invalid"
        );
        AssertUtil.assertNotBlank(application.getType(), "application type is null");
        AssertUtil.assertNotNull(application.getOrganizationId(), "application organization is null");
    }

}
