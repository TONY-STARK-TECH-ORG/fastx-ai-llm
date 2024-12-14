package com.fastx.ai.llm.domains.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fastx.ai.llm.domains.entity.KnowledgeBaseFile;
import com.fastx.ai.llm.domains.mapper.KnowledgeBaseFileMapper;
import com.fastx.ai.llm.domains.service.IKnowledgeBaseFileService;
import com.rometools.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
@Service
public class KnowledgeBaseFileServiceImpl extends ServiceImpl<KnowledgeBaseFileMapper, KnowledgeBaseFile> implements IKnowledgeBaseFileService {

    @Override
    public List<KnowledgeBaseFile> getKnowledgeBaseFileByKnowledgeBaseId(Long knowledgeBaseId) {
        LambdaQueryWrapper<KnowledgeBaseFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KnowledgeBaseFile::getKnowledgeBaseId, knowledgeBaseId);
        queryWrapper.orderByDesc(KnowledgeBaseFile::getCreateTime);
        return Lists.createWhenNull(this.list(queryWrapper));
    }

    @Override
    public boolean removeFilesByKnowledgeBaseId(Long knowledgeId) {
        LambdaQueryWrapper<KnowledgeBaseFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgeBaseFile::getKnowledgeBaseId, knowledgeId);
        if (count(wrapper) > 0) {
            return this.remove(wrapper);
        }
        return true;
    }

}
