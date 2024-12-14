package com.fastx.ai.llm.domains.service;

import com.fastx.ai.llm.domains.entity.KnowledgeBase;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IKnowledgeBaseService extends IService<KnowledgeBase> {

    /**
     * get knowledge base by org id
     * @param organizationIds org ids
     * @return list
     */
    List<KnowledgeBase> getKnowledgeBaseByOrganizationIds(List<Long> organizationIds);
}
