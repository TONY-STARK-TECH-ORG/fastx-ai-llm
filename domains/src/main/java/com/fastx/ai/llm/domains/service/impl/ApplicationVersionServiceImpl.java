package com.fastx.ai.llm.domains.service.impl;

import com.alibaba.csp.sentinel.util.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.entity.ApplicationVersion;
import com.fastx.ai.llm.domains.mapper.ApplicationVersionMapper;
import com.fastx.ai.llm.domains.service.IApplicationVersionService;
import com.rometools.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
@Service
public class ApplicationVersionServiceImpl extends ServiceImpl<ApplicationVersionMapper, ApplicationVersion> implements IApplicationVersionService {

    @Override
    public List<ApplicationVersion> findByAppId(Long appId) {
        LambdaQueryWrapper<ApplicationVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApplicationVersion::getApplicationId, appId);
        queryWrapper.orderByDesc(ApplicationVersion::getCreateTime);
        return Lists.createWhenNull(this.baseMapper.selectList(queryWrapper));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean activeVersion(Long appVersionId) {
        // query version
        ApplicationVersion applicationVersion = this.getById(appVersionId);
        AssertUtil.notNull(applicationVersion, "applicationVersion is null");

        Long applicationId = applicationVersion.getApplicationId();
        // deactivate other versions
        LambdaUpdateWrapper<ApplicationVersion> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ApplicationVersion::getApplicationId, applicationId);
        updateWrapper.set(ApplicationVersion::getStatus, IConstant.IN_ACTIVE);
        this.baseMapper.update(updateWrapper);
        // active current version
        applicationVersion.setStatus(IConstant.ACTIVE);
        return this.updateById(applicationVersion);
    }

    @Override
    public boolean inactiveVersion(Long appVersionId) {
        // query version
        ApplicationVersion applicationVersion = this.getById(appVersionId);
        AssertUtil.notNull(applicationVersion, "applicationVersion is null");
        // deactivate
        applicationVersion.setStatus(IConstant.IN_ACTIVE);
        return this.updateById(applicationVersion);
    }

    @Override
    public ApplicationVersion createVersion(Long applicationId, String versionData, String version) {
        ApplicationVersion applicationVersion = new ApplicationVersion();
        applicationVersion.setApplicationId(applicationId);
        applicationVersion.setVersionData(versionData);
        applicationVersion.setVersion(version);
        applicationVersion.setStatus(IConstant.IN_ACTIVE);
        AssertUtil.assertTrue(this.save(applicationVersion), "applicationVersion save failed");
        return applicationVersion;
    }

    @Override
    public boolean updateVersionData(Long applicationVersionId, String versionData) {
        LambdaUpdateWrapper<ApplicationVersion> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ApplicationVersion::getId, applicationVersionId);
        updateWrapper.set(ApplicationVersion::getVersionData, versionData);
        return this.update(updateWrapper);
    }
}
