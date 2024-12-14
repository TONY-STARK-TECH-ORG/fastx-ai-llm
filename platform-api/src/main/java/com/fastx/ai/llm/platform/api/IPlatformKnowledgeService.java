package com.fastx.ai.llm.platform.api;

import com.fastx.ai.llm.platform.dto.KnowledgeDTO;
import com.fastx.ai.llm.platform.dto.KnowledgeFileDTO;

import java.util.List;

/**
 * @author stark
 */
public interface IPlatformKnowledgeService {

    /**
     * get kb under user id
     * @param userId user id
     * @return kb list
     */
    List<KnowledgeDTO> getKnowledgesByUserId(Long userId);

    /**
     * get kb under id
     * @param knowledgeId kb id
     * @return kb list
     */
    KnowledgeDTO getKnowledgeById(Long knowledgeId);

    /**
     * get kb files
     * @param knowledgeId kb id
     * @return all files under kb
     */
    List<KnowledgeFileDTO> getKnowledgeFilesByKnowledgeId(Long knowledgeId);

    /**
     * create knowledge
     * @param knowledge kb info
     * @return created kb
     */
    KnowledgeDTO createKnowledge(KnowledgeDTO knowledge);

    /**
     * create kb files with list
     * @param knowledgeFiles files
     * @return created files
     */
    List<KnowledgeFileDTO> createKnowledgeFile(List<KnowledgeFileDTO> knowledgeFiles);

    /**
     * update kb
     * @param knowledge info
     * @return true or false
     */
    boolean updateKnowledge(KnowledgeDTO knowledge);

    /**
     * delete kb
     * @param knowledgeId kb id
     * @return true or false
     */
    boolean deleteKnowledge(Long knowledgeId);

    /**
     * delete kb file
     * @param knowledgeFileIds file ids
     * @return true or false
     */
    boolean deleteKnowledgeFile(List<Long> knowledgeFileIds);

}
