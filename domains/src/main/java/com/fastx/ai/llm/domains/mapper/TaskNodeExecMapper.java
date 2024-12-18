package com.fastx.ai.llm.domains.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastx.ai.llm.domains.entity.TaskNodeExec;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface TaskNodeExecMapper extends BaseMapper<TaskNodeExec> {

    List<TaskNodeExec> getParentChainTaskNodeExecByNodeId(@Param("nodeId") String nodeId);

    List<TaskNodeExec> getParentTaskNodeExec(@Param("nodeId") String nodeId);
}
