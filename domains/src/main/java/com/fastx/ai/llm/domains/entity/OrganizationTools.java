package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.OrganizationToolsDTO;
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
@TableName("t_organization_tools")
public class OrganizationTools extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long organizationId;

    private String toolCode;

    private String toolVersion;

    private String configData;

    private Boolean custom;

    private String customCode;

    public static OrganizationTools of(OrganizationToolsDTO organization) {
        OrganizationTools organizationTools = new OrganizationTools();
        BeanUtils.copyProperties(organization, organizationTools);
        return organizationTools;
    }

    public OrganizationToolsDTO to() {
        OrganizationToolsDTO organizationToolsDTO = new OrganizationToolsDTO();
        BeanUtils.copyProperties(this, organizationToolsDTO);
        return organizationToolsDTO;
    }
}
