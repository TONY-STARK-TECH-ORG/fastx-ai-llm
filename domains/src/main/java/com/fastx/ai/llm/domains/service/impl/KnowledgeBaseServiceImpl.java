package com.fastx.ai.llm.domains.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fastx.ai.llm.domains.entity.KnowledgeBase;
import com.fastx.ai.llm.domains.mapper.KnowledgeBaseMapper;
import com.fastx.ai.llm.domains.service.IKnowledgeBaseService;
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
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBase> implements IKnowledgeBaseService {

    @Override
    public List<KnowledgeBase> getKnowledgeBaseByOrganizationIds(List<Long> organizationIds) {
        LambdaQueryWrapper<KnowledgeBase> lqw = new LambdaQueryWrapper<>();
        lqw.in(KnowledgeBase::getOrganizationId, organizationIds);
        lqw.orderByDesc(KnowledgeBase::getCreateTime);
        return Lists.createWhenNull(this.list(lqw));
    }

}
