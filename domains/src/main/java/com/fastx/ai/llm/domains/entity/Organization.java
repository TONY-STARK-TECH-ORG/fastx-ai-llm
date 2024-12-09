package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.OrganizationDTO;
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
@TableName("t_organization")
public class Organization extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    public OrganizationDTO to() {
        OrganizationDTO dto = new OrganizationDTO();
        BeanUtils.copyProperties(this, dto);
        return dto;
    }
}
