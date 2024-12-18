package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.TaskNodeExecDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
@Getter
@Setter
@TableName("t_task_node_exec")
public class TaskNodeExec extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long taskExecId;

    private String nodeId;

    private String nextNodeIds;

    private String startTime;

    private String endTime;

    private String status;

    private String inputs;

    private String outputs;

    private String config;

    public static TaskNodeExec of(TaskNodeExecDTO taskNodeExecDTO) {
        TaskNodeExec task = new TaskNodeExec();
        BeanUtils.copyProperties(taskNodeExecDTO, task);
        return task;
    }

    public TaskNodeExecDTO to() {
        TaskNodeExecDTO taskNodeExecDTO = new TaskNodeExecDTO();
        BeanUtils.copyProperties(this, taskNodeExecDTO);
        return taskNodeExecDTO;
    }
}
