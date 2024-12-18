package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.TaskExecDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("t_task_exec")
public class TaskExec extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long taskId;

    private String status;

    private LocalDateTime completeTime;

    private String log;

    public static TaskExec of(TaskExecDTO taskExecDTO) {
        TaskExec taskExec = new TaskExec();
        BeanUtils.copyProperties(taskExecDTO, taskExec);
        return taskExec;
    }

    public TaskExecDTO to() {
        TaskExecDTO taskExecDTO = new TaskExecDTO();
        BeanUtils.copyProperties(this, taskExecDTO);
        return taskExecDTO;
    }
}
