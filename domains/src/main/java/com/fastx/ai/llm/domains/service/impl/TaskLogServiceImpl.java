package com.fastx.ai.llm.domains.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fastx.ai.llm.domains.entity.TaskLog;
import com.fastx.ai.llm.domains.mapper.TaskLogMapper;
import com.fastx.ai.llm.domains.service.ITaskLogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
public class TaskLogServiceImpl extends ServiceImpl<TaskLogMapper, TaskLog> implements ITaskLogService {

    @Override
    public Page<TaskLog> getTaskLogsByTaskId(Long taskId, Long page, Long size, String status) {
        LambdaQueryWrapper<TaskLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TaskLog::getTaskId, taskId);
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq(TaskLog::getStatus, status);
        }
        wrapper.orderByDesc(TaskLog::getCreateTime);
        Page<TaskLog> paged = this.page(new Page<>(page, size), wrapper);
        if (CollectionUtils.isEmpty(paged.getRecords())) {
            paged.setRecords(List.of());
        }
        return paged;
    }

    @Override
    public boolean removeLogsByTaskId(Long taskId) {
        LambdaQueryWrapper<TaskLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TaskLog::getTaskId, taskId);
        if (count(wrapper) > 0) {
            return this.remove(wrapper);
        }
        return true;
    }
}
