package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.KnowledgeBaseDTO;
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
@TableName("t_knowledge_base")
public class KnowledgeBase extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private Long organizationId;

    private String status;

    public static KnowledgeBase of(KnowledgeBaseDTO knowledgeBase) {
        KnowledgeBase kb = new KnowledgeBase();
        BeanUtils.copyProperties(knowledgeBase, kb);
        return kb;
    }

    public KnowledgeBaseDTO to() {
        KnowledgeBaseDTO dto = new KnowledgeBaseDTO();
        BeanUtils.copyProperties(this, dto);
        return dto;
    }
}
