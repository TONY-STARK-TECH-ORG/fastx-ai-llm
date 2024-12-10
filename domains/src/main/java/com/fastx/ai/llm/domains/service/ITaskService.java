package com.fastx.ai.llm.domains.service;

import com.fastx.ai.llm.domains.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface ITaskService extends IService<Task> {

    /**
     * get all task under org id
     * @param organizationId org id
     * @return task list
     */
    List<Task> getTasksByOrganizationId(Long organizationId);
}
