package com.fastx.ai.llm.domains.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fastx.ai.llm.domains.entity.OrganizationTools;
import com.fastx.ai.llm.domains.mapper.OrganizationToolsMapper;
import com.fastx.ai.llm.domains.service.IOrganizationToolsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class OrganizationToolsServiceImpl extends ServiceImpl<OrganizationToolsMapper, OrganizationTools> implements IOrganizationToolsService {

    @Override
    public List<OrganizationTools> getOrganizationToolsByOrganizationId(Long organizationId) {
        LambdaQueryWrapper<OrganizationTools> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrganizationTools::getOrganizationId, organizationId);
        wrapper.orderByDesc(OrganizationTools::getCreateTime);
        return Lists.createWhenNull(list(wrapper));
    }
}
