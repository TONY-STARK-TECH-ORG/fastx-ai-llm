package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.ApplicationVersionDTO;
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
@TableName("t_application_version")
public class ApplicationVersion extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long applicationId;

    private String version;

    private String status;

    private String versionData;

    public ApplicationVersionDTO to() {
        ApplicationVersionDTO dto = new ApplicationVersionDTO();
        BeanUtils.copyProperties(this, dto);
        return dto;
    }
}
