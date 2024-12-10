package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.KnowledgeBaseFileDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
@Getter
@Setter
@TableName("t_knowledge_base_file")
public class KnowledgeBaseFile extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long knowledgeBaseId;

    private String name;

    private String extension;

    private String downloadUrl;

    private String vecCollectionName;

    private String vecCollectionId;

    private String vecPartitionKey;

    private String vecPartitionValue;

    private String status;

    public static KnowledgeBaseFile of(KnowledgeBaseFileDTO knowledgeBaseFileDTO) {
        KnowledgeBaseFile knowledgeBaseFile = new KnowledgeBaseFile();
        BeanUtils.copyProperties(knowledgeBaseFileDTO, knowledgeBaseFile);
        return knowledgeBaseFile;
    }

    public KnowledgeBaseFileDTO to() {
        KnowledgeBaseFileDTO knowledgeBaseFileDTO = new KnowledgeBaseFileDTO();
        BeanUtils.copyProperties(this, knowledgeBaseFileDTO);
        return knowledgeBaseFileDTO;
    }
}
