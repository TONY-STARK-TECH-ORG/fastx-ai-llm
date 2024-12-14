package com.fastx.ai.llm.domains.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fastx.ai.llm.domains.entity.Task;
import com.fastx.ai.llm.domains.mapper.TaskMapper;
import com.fastx.ai.llm.domains.service.ITaskService;
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
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Override
    public List<Task> getTasksByOrganizationId(Long organizationId) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getOrganizationId, organizationId);
        wrapper.orderByDesc(Task::getCreateTime);
        return Lists.createWhenNull(list(wrapper));
    }
}
