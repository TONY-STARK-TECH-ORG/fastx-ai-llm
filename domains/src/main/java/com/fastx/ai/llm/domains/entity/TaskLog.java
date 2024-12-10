package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.TaskLogDTO;
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
@TableName("t_task_log")
public class TaskLog extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long taskId;

    private String status;

    private LocalDateTime completeTime;

    public static TaskLog of(TaskLogDTO taskLogDTO) {
        TaskLog taskLog = new TaskLog();
        BeanUtils.copyProperties(taskLogDTO, taskLog);
        return taskLog;
    }

    public TaskLogDTO to() {
        TaskLogDTO taskLogDTO = new TaskLogDTO();
        BeanUtils.copyProperties(this, taskLogDTO);
        return taskLogDTO;
    }
}
