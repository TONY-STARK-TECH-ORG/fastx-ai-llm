package com.fastx.ai.llm.domains.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fastx.ai.llm.domains.entity.KnowledgeBaseFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IKnowledgeBaseFileService extends IService<KnowledgeBaseFile> {

    /**
     * get all files under kb
     * @param knowledgeBaseId kb id
     * @return file list
     */
    List<KnowledgeBaseFile> getKnowledgeBaseFileByKnowledgeBaseId(Long knowledgeBaseId);

    /**
     * remove files by Knowledge id
     * @param knowledgeId knowledge id
     * @return true or false
     */
    boolean removeFilesByKnowledgeBaseId(Long knowledgeId);
}
