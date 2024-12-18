package com.fastx.ai.llm.domains.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fastx.ai.llm.domains.entity.TaskExec;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface ITaskExecService extends IService<TaskExec> {

    /**
     * get task logs by id and page params
     * @param taskId task id
     * @param page page
     * @param size size
     * @param status status
     * @return list with page info
     */
    Page<TaskExec> getTaskExecs(Long taskId, Long page, Long size, String status);

    /**
     * remote logs under a task
     * @param taskId task id
     * @return true or false
     */
    boolean removeExecsByTaskId(Long taskId);
}
