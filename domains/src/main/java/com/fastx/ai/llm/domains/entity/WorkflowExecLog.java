package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.WorkflowExecLogDTO;
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
@TableName("t_workflow_exec_log")
public class WorkflowExecLog extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long workflowVersionId;

    private String inputData;

    private String outputData;

    private String execData;

    private String extraData;

    public static WorkflowExecLog of(WorkflowExecLogDTO workflowExecLogDTO) {
        WorkflowExecLog execLog = new WorkflowExecLog();
        BeanUtils.copyProperties(workflowExecLogDTO, execLog);
        return execLog;
    }

    public WorkflowExecLogDTO to() {
        WorkflowExecLogDTO dto = new WorkflowExecLogDTO();
        BeanUtils.copyProperties(this, dto);
        return dto;
    }
}
