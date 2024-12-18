package com.fastx.ai.llm.domains.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fastx.ai.llm.domains.entity.TaskNodeExec;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface ITaskNodeExecService extends IService<TaskNodeExec> {

    /**
     * list all node execs under task exec.
     * @param taskExecId task exec id
     * @return node exec list.
     */
    List<TaskNodeExec> getTaskNodeExecs(Long taskExecId);

    /**
     * page search exec nodes.
     * @param page page
     * @param size size
     * @param status status
     * @param checkPrevNodes check prev.
     * @return result
     */
    Page<TaskNodeExec> getTaskNodeExecs(
            Long page, Long size, String status, Boolean checkPrevNodes);

    /**
     * remove by task exec id
     * @param taskExecId task exec id
     * @return true or false
     */
    boolean removeByExecId(Long taskExecId);

    /**
     * get executable nodes by node id.
     * @param nodeId node id
     * @return node list
     */
    List<TaskNodeExec> getParentChainTaskNodeExecByNodeId(String nodeId);

    /**
     * get parent nodes by node id.
     * @param nodeId
     * @return
     */
    List<TaskNodeExec> getParentTaskNodeExec(String nodeId);
}
