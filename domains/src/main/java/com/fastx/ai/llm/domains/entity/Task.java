package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import lombok.Getter;
import lombok.Setter;

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

    private Long workflowId;

    private String status;
}
