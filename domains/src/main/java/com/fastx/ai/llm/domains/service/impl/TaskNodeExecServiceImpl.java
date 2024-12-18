package com.fastx.ai.llm.domains.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fastx.ai.llm.domains.entity.TaskNodeExec;
import com.fastx.ai.llm.domains.mapper.TaskNodeExecMapper;
import com.fastx.ai.llm.domains.service.ITaskNodeExecService;
import com.rometools.utils.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
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
public class TaskNodeExecServiceImpl extends ServiceImpl<TaskNodeExecMapper, TaskNodeExec> implements ITaskNodeExecService {

    @Override
    public List<TaskNodeExec> getTaskNodeExecs(Long taskExecId) {
        LambdaQueryWrapper<TaskNodeExec> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaskNodeExec::getTaskExecId, taskExecId);
        queryWrapper.orderByDesc(TaskNodeExec::getCreateTime);
        return Lists.createWhenNull(list(queryWrapper));
    }

    @Override
    public Page<TaskNodeExec> getTaskNodeExecs(Long page, Long size, String status, Boolean checkPrevNodes) {
        LambdaQueryWrapper<TaskNodeExec> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaskNodeExec::getStatus, status);
        queryWrapper.orderByDesc(TaskNodeExec::getCreateTime);
        if (BooleanUtils.isTrue(checkPrevNodes)) {
            // query prev node first and check!
            // select count() xxx.next_node_id contains node_id && status == wait || running
        }
        Page<TaskNodeExec> paged = page(new Page<>(page, size), queryWrapper);
        if (CollectionUtils.isEmpty(paged.getRecords())) {
            paged.setRecords(List.of());
        }
        return paged;
    }

    @Override
    public boolean removeByExecId(Long taskExecId) {
        LambdaQueryWrapper<TaskNodeExec> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaskNodeExec::getTaskExecId, taskExecId);
        return remove(queryWrapper);
    }
}
