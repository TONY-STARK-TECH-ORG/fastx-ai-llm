package com.fastx.ai.llm.domains.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fastx.ai.llm.domains.entity.Workflow;
import com.fastx.ai.llm.domains.mapper.WorkflowMapper;
import com.fastx.ai.llm.domains.service.IWorkflowService;
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
public class WorkflowServiceImpl extends ServiceImpl<WorkflowMapper, Workflow> implements IWorkflowService {

    @Override
    public List<Workflow> getWorkflowsByOrganizationId(Long organizationId) {
        LambdaQueryWrapper<Workflow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Workflow::getOrganizationId, organizationId);
        wrapper.orderByDesc(Workflow::getCreateTime);
        return Lists.emptyToNull(this.list(wrapper));
    }
}
