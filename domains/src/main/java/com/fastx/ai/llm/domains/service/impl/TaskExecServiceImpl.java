package com.fastx.ai.llm.domains.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fastx.ai.llm.domains.entity.TaskExec;
import com.fastx.ai.llm.domains.mapper.TaskExecMapper;
import com.fastx.ai.llm.domains.service.ITaskExecService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
@Service
public class TaskExecServiceImpl extends ServiceImpl<TaskExecMapper, TaskExec> implements ITaskExecService {

    @Override
    public Page<TaskExec> getTaskExecs(Long taskId, Long page, Long size, String status, String type) {
        LambdaQueryWrapper<TaskExec> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(taskId)) {
            wrapper.eq(TaskExec::getTaskId, taskId);
        }
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq(TaskExec::getStatus, status);
        }
        // @TODO (yedong) select t_task t where t.type = type
        wrapper.orderByDesc(TaskExec::getCreateTime);
        Page<TaskExec> paged = this.page(new Page<>(page, size), wrapper);
        if (CollectionUtils.isEmpty(paged.getRecords())) {
            paged.setRecords(List.of());
        }
        return paged;
    }

    @Override
    public boolean removeExecsByTaskId(Long taskId) {
        LambdaQueryWrapper<TaskExec> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TaskExec::getTaskId, taskId);
        if (count(wrapper) > 0) {
            return this.remove(wrapper);
        }
        return true;
    }
}
