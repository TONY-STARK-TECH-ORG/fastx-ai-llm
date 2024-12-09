package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.ApplicationDTO;
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
@TableName("t_application")
public class Application extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String type;

    private String description;

    private String iconUrl;

    private String status;

    private Long organizationId;

    public static Application of(ApplicationDTO applicationDTO) {
        Application application = new Application();
        BeanUtils.copyProperties(applicationDTO, application);
        return application;
    }

    public ApplicationDTO to() {
        ApplicationDTO applicationDTO = new ApplicationDTO();
        BeanUtils.copyProperties(this, applicationDTO);
        return applicationDTO;
    }
}
