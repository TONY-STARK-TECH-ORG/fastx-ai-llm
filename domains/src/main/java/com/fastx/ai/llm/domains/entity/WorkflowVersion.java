package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.WorkflowVersionDTO;
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
@TableName("t_workflow_version")
public class WorkflowVersion extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long workflowId;

    private String version;

    private String status;

    private String versionData;

    public static WorkflowVersion of(WorkflowVersionDTO workflowVersionDTO) {
        WorkflowVersion version = new WorkflowVersion();
        BeanUtils.copyProperties(workflowVersionDTO, version);
        return version;
    }

    public WorkflowVersionDTO to() {
        WorkflowVersionDTO workflowVersionDTO = new WorkflowVersionDTO();
        BeanUtils.copyProperties(this, workflowVersionDTO);
        return workflowVersionDTO;
    }
}
