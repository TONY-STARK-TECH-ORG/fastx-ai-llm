package com.fastx.ai.llm.domains.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fastx.ai.llm.domains.entity.Application;
import com.fastx.ai.llm.domains.mapper.ApplicationMapper;
import com.fastx.ai.llm.domains.service.IApplicationService;
import com.rometools.utils.Lists;
import org.springframework.stereotype.Service;

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
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements IApplicationService {

    @Override
    public List<Application> findByOrgIds(List<Long> orgIds) {
        LambdaQueryWrapper<Application> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Application::getOrganizationId, orgIds);
        queryWrapper.orderByDesc(Application::getCreateTime);
        return Lists.createWhenNull(baseMapper.selectList(queryWrapper));
    }
}
