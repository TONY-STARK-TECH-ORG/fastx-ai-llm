package com.fastx.ai.llm.domains.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fastx.ai.llm.domains.entity.WorkflowVersion;
import com.fastx.ai.llm.domains.mapper.WorkflowVersionMapper;
import com.fastx.ai.llm.domains.service.IWorkflowVersionService;
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
public class WorkflowVersionServiceImpl extends ServiceImpl<WorkflowVersionMapper, WorkflowVersion> implements IWorkflowVersionService {

    @Override
    public List<WorkflowVersion> getWorkflowVersionByWorkflowId(Long workflowId) {
        LambdaQueryWrapper<WorkflowVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkflowVersion::getWorkflowId, workflowId);
        queryWrapper.orderByDesc(WorkflowVersion::getCreateTime);
        return Lists.createWhenNull(this.list(queryWrapper));
    }

}
