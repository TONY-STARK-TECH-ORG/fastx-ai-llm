package com.fastx.ai.llm.domains.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fastx.ai.llm.domains.entity.OrganizationUser;
import com.fastx.ai.llm.domains.mapper.OrganizationUserMapper;
import com.fastx.ai.llm.domains.service.IOrganizationUserService;
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
public class OrganizationUserServiceImpl extends ServiceImpl<OrganizationUserMapper, OrganizationUser> implements IOrganizationUserService {

    @Override
    public List<OrganizationUser> findByUserId(Long userId) {
        LambdaQueryWrapper<OrganizationUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrganizationUser::getUserId, userId);
        queryWrapper.orderByDesc(OrganizationUser::getCreateTime);
        return Lists.createWhenNull(this.baseMapper.selectList(queryWrapper));
    }

}
