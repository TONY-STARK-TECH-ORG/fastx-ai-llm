package com.fastx.ai.llm.domains.service.impl;

import com.fastx.ai.llm.domains.entity.Task;
import com.fastx.ai.llm.domains.mapper.TaskMapper;
import com.fastx.ai.llm.domains.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
