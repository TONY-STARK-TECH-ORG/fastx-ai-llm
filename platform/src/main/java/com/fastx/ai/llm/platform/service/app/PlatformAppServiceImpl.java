package com.fastx.ai.llm.platform.service.app;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.api.IDubboApplicationService;
import com.fastx.ai.llm.domains.api.IDubboOrganizationService;
import com.fastx.ai.llm.domains.dto.ApplicationDTO;
import com.fastx.ai.llm.domains.dto.ApplicationVersionDTO;
import com.fastx.ai.llm.domains.dto.OrganizationDTO;
import com.fastx.ai.llm.platform.api.IPlatformAppService;
import com.fastx.ai.llm.platform.constant.IConstant;
import com.fastx.ai.llm.platform.dto.AppDTO;
import com.fastx.ai.llm.platform.dto.AppVersionDTO;
import com.fastx.ai.llm.platform.dto.OrgDTO;
import com.rometools.utils.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author stark
 */
@DubboService
public class PlatformAppServiceImpl implements IPlatformAppService {

    @DubboReference
    IDubboApplicationService dubboApplicationService;

    @DubboReference
    IDubboOrganizationService dubboOrganizationService;

    @Override
    @SentinelResource("platform.app.get")
    public List<AppDTO> getAppList(Long userId) {
        List<ApplicationDTO> applications = Lists.createWhenNull(dubboApplicationService.getApplications(userId));
        return applications.stream().map(applicationDTO -> {
            AppDTO appDTO = new AppDTO();
            BeanUtils.copyProperties(applicationDTO, appDTO);
            // query other basic infos
            OrganizationDTO organization =
                    dubboOrganizationService.findByOrganizationId(applicationDTO.getOrganizationId());
            OrgDTO orgDTO = new OrgDTO();
            BeanUtils.copyProperties(organization, orgDTO);
            appDTO.setOrganization(orgDTO);
            // query app version
            List<ApplicationVersionDTO> applicationVersions =
                    dubboApplicationService.getApplicationVersions(applicationDTO.getId());
            if (CollectionUtils.isNotEmpty(applicationVersions)) {
                appDTO.setApplicationVersions(
                        applicationVersions.stream().map(av -> {
                            String versionData = av.getVersionData();
                            AppVersionDTO appVersionDTO = new AppVersionDTO();
                            appVersionDTO.setConfig(versionData);
                            BeanUtils.copyProperties(av, appVersionDTO);

                            if (appVersionDTO.getStatus().equals(IConstant.ACTIVE)) {
                                appDTO.setActiveVersion(appVersionDTO);
                            }
                            return appVersionDTO;
                        }).collect(Collectors.toList())
                );
            }
            return appDTO;
        }).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("platform.app.create")
    @GlobalTransactional
    public AppDTO createApp(AppDTO app) {
        isValidated(app);
        OrganizationDTO organizationDTO = isValidatedOrganization(app.getOrganizationId());
        // create dto
        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setName(app.getName());
        applicationDTO.setDescription(app.getDescription());
        applicationDTO.setIconUrl(app.getIconUrl());
        applicationDTO.setType(app.getType());
        applicationDTO.setOrganizationId(app.getOrganizationId());
        // set app basic info
        BeanUtils.copyProperties(dubboApplicationService.createApplication(applicationDTO), app);
        // set organization
        OrgDTO orgDTO = new OrgDTO();
        BeanUtils.copyProperties(organizationDTO, orgDTO);
        app.setOrganization(orgDTO);
        return app;
    }

    @Override
    @SentinelResource("platform.app.update")
    public Boolean updateApp(AppDTO appDTO) {
        Assert.notNull(appDTO, "app is null");
        Assert.notNull(appDTO.getId(), "app is null");

        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setId(appDTO.getId());
        // updated data
        applicationDTO.setName(appDTO.getName());
        applicationDTO.setDescription(appDTO.getDescription());
        applicationDTO.setIconUrl(appDTO.getIconUrl());
        applicationDTO.setType(appDTO.getType());
        applicationDTO.setOrganizationId(appDTO.getOrganizationId());
        applicationDTO.setStatus(appDTO.getStatus());
        // update
        return dubboApplicationService.updateApplication(applicationDTO);
    }

    @Override
    @SentinelResource("platform.app.version.create")
    public AppVersionDTO createNewAppVersion(AppVersionDTO appVersionDTO) {
        String versionData = appVersionDTO.getConfig();
        ApplicationVersionDTO version =
                dubboApplicationService.createVersionData(appVersionDTO.getApplicationId(), versionData, appVersionDTO.getVersion());
        AppVersionDTO dto = new AppVersionDTO();
        BeanUtils.copyProperties(version, dto);
        dto.setConfig(versionData);
        return dto;
    }

    @Override
    @SentinelResource("platform.app.version.update")
    public Boolean updateAppVersion(AppVersionDTO appVersionDTO) {
        Assert.notNull(appVersionDTO, "appVersion is null");
        Assert.notNull(appVersionDTO.getId(), "appVersion is null");
        String versionData = appVersionDTO.getConfig();
        return dubboApplicationService.updateVersionData(appVersionDTO.getId(), versionData);
    }

    @Override
    @SentinelResource("platform.app.version.update")
    public Boolean activateAppVersion(AppVersionDTO appVersionDTO) {
        Assert.notNull(appVersionDTO, "appVersion is null");
        Assert.notNull(appVersionDTO.getId(), "appVersion is null");
        // active current version and in active other version
        return dubboApplicationService.activeVersion(appVersionDTO.getId());
    }

    @Override
    @SentinelResource("platform.app.version.update")
    public Boolean inactivateAppVersion(AppVersionDTO appVersionDTO) {
        Assert.notNull(appVersionDTO, "appVersion is null");
        Assert.notNull(appVersionDTO.getId(), "appVersion is null");
        // only de active current version
        return dubboApplicationService.inactiveVersion(appVersionDTO.getId());
    }

    @Override
    @SentinelResource("platform.app.delete")
    public Boolean deleteApp(Long appId) {
        Assert.notNull(appId, "appId is null");
        return dubboApplicationService.deleteApplication(appId);
    }

    @Override
    @SentinelResource("platform.app.version.delete")
    public Boolean deleteAppVersion(Long appVersionId) {
        Assert.notNull(appVersionId, "appVersionId is null");
        return dubboApplicationService.deleteVersion(appVersionId);
    }

    @Override
    @SentinelResource("platform.app.version.get")
    public List<AppVersionDTO> getAppVersionList(Long appId) {
        Assert.notNull(appId, "appId is null");
        List<ApplicationVersionDTO> applicationVersions = dubboApplicationService.getApplicationVersions(appId);
        return Lists.createWhenNull(applicationVersions).stream().map(av -> {
            String versionData = av.getVersionData();

            AppVersionDTO appVersionDTO = new AppVersionDTO();
            appVersionDTO.setConfig(versionData);
            BeanUtils.copyProperties(av, appVersionDTO);
            return appVersionDTO;
        }).collect(Collectors.toList());
    }

    private OrganizationDTO isValidatedOrganization(Long organizationId) {
        OrganizationDTO organizationDTO = dubboOrganizationService.findByOrganizationId(organizationId);
        Assert.notNull(organizationDTO, "organization not found");
        return organizationDTO;
    }

    private boolean isValidated(AppDTO app) {
        Assert.notNull(app, "app is null");
        Assert.notNull(app.getName(), "app.name is null");
        Assert.notNull(app.getDescription(), "app.description is null");
        Assert.notNull(app.getIconUrl(), "app.iconUrl is null");
        Assert.notNull(app.getType(), "app.type is null");
        Assert.notNull(app.getOrganizationId(), "app.organizationId is null");
        return true;
    }

}
