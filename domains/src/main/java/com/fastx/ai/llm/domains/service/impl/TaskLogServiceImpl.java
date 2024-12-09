package com.fastx.ai.llm.domains.service.impl;

import com.fastx.ai.llm.domains.entity.TaskLog;
import com.fastx.ai.llm.domains.mapper.TaskLogMapper;
import com.fastx.ai.llm.domains.service.ITaskLogService;
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
public class TaskLogServiceImpl extends ServiceImpl<TaskLogMapper, TaskLog> implements ITaskLogService {

}
