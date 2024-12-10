package com.fastx.ai.llm.domains.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastx.ai.llm.domains.entity.WorkflowExecLog;
import com.fastx.ai.llm.domains.mapper.WorkflowExecLogMapper;
import com.fastx.ai.llm.domains.service.IWorkflowExecLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
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
public class WorkflowExecLogServiceImpl extends ServiceImpl<WorkflowExecLogMapper, WorkflowExecLog> implements IWorkflowExecLogService {

    @Override
    public boolean removeWorkflowExecLogByWorkflowVersionIds(List<Long> versionIds) {
        LambdaQueryWrapper<WorkflowExecLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(WorkflowExecLog::getWorkflowVersionId, versionIds);
        long count = this.count(queryWrapper);
        if (count > 0) {
            return this.remove(queryWrapper);
        }
        return true;
    }

    @Override
    public Page<WorkflowExecLog> getWorkflowExecLogsByWorkflowVersionId(Long workflowVersionId, Long page, Long size) {
        LambdaQueryWrapper<WorkflowExecLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkflowExecLog::getWorkflowVersionId, workflowVersionId);
        wrapper.orderByDesc(WorkflowExecLog::getCreateTime);
        Page<WorkflowExecLog> execLogPage = this.page(new Page<>(page, size), wrapper);
        if (CollectionUtils.isEmpty(execLogPage.getRecords())) {
            execLogPage.setRecords(List.of());
        }
        return execLogPage;
    }
}
