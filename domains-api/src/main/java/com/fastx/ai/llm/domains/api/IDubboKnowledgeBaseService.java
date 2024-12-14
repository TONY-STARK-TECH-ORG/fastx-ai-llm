package com.fastx.ai.llm.domains.api;

import com.fastx.ai.llm.domains.dto.KnowledgeBaseDTO;
import com.fastx.ai.llm.domains.dto.KnowledgeBaseFileDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IDubboKnowledgeBaseService {

    /**
     * create knowledge base
     * @param knowledgeBaseDTO info
     * @return created kb
     */
    KnowledgeBaseDTO createKnowledgeBase(KnowledgeBaseDTO knowledgeBaseDTO);

    /**
     * update kn info
     * @param knowledgeBaseDTO info
     * @return true or false
     */
    boolean updateKnowledgeBase(KnowledgeBaseDTO knowledgeBaseDTO);

    /**
     * get single kb
     * @param id kb id
     * @return kb
     */
    KnowledgeBaseDTO getKnowledgeBase(Long id);

    /**
     * get all kb under org
     * @param userId user id
     * @return kb list
     */
    List<KnowledgeBaseDTO> getKnowledgeBaseByUserId(Long userId);

    /**
     * delete single kb, with all under kb-files and milvus data.
     * @param id kb id
     * @return true or false
     */
    boolean deleteKnowledgeBase(Long id);

    // ----------------------------------------------------------------
    // knowledge base file options
    // ----------------------------------------------------------------

    /**
     * get single file
     * @param id file id
     * @return file obj
     */
    KnowledgeBaseFileDTO getKnowledgeBaseFile(Long id);

    /**
     * get all files under kb
     * @param knowledgeBaseId kb id
     * @return all file list
     */
    List<KnowledgeBaseFileDTO> getKnowledgeBaseFileByKnowledgeBaseId(Long knowledgeBaseId);

    /**
     * batch create knowledge base file
     * @param knowledgeBaseFileDTOs infos
     * @return created list
     */
    List<KnowledgeBaseFileDTO> batchCreateKnowledgeBaseFiles(List<KnowledgeBaseFileDTO> knowledgeBaseFileDTOs);

    /**
     * delete single file
     * @param id file id
     * @return true or false
     */
    boolean deleteKnowledgeBaseFile(Long id);

    /**
     * batch delete file
     * @param ids file id list
     * @return true or false
     */
    boolean batchDeleteKnowledgeBaseFiles(List<Long> ids);
}
