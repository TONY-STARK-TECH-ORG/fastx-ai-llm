package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.WorkflowDTO;
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
@TableName("t_workflow")
public class Workflow extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String status;

    private Long organizationId;

    public static Workflow of(WorkflowDTO workflowDTO) {
        Workflow workflow = new Workflow();
        BeanUtils.copyProperties(workflowDTO, workflow);
        return workflow;
    }

    public WorkflowDTO to() {
        WorkflowDTO workflowDTO = new WorkflowDTO();
        BeanUtils.copyProperties(this, workflowDTO);
        return workflowDTO;
    }
}
