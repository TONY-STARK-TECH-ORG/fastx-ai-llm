package com.fastx.ai.llm.domains.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fastx.ai.llm.domains.entity.Organization;
import com.fastx.ai.llm.domains.mapper.OrganizationMapper;
import com.fastx.ai.llm.domains.service.IOrganizationService;
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
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

    @Override
    public List<Organization> findByUserId(Long userId) {
        return Lists.emptyToNull(this.baseMapper.findByUserId(userId));
    }

}
