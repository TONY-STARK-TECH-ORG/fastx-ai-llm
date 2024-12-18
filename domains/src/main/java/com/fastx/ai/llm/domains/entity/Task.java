package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.TaskDTO;
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
@TableName("t_task")
public class Task extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long organizationId;

    private String name;

    private String description;

    private String cron;

    private Long workflowVersionId;

    private String status;

    private String type;

    public static Task of(TaskDTO taskDTO) {
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task);
        return task;
    }

    public TaskDTO to() {
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(this, taskDTO);
        return taskDTO;
    }
}
