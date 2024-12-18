package com.fastx.ai.llm.platform.service.knowledge;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.api.IDubboKnowledgeBaseService;
import com.fastx.ai.llm.domains.dto.KnowledgeBaseDTO;
import com.fastx.ai.llm.domains.dto.KnowledgeBaseFileDTO;
import com.fastx.ai.llm.platform.api.IPlatformKnowledgeService;
import com.fastx.ai.llm.platform.dto.KnowledgeDTO;
import com.fastx.ai.llm.platform.dto.KnowledgeFileDTO;
import com.rometools.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author stark
 */
@DubboService
public class PlatformKnowledgeServiceImpl implements IPlatformKnowledgeService {

    @DubboReference
    IDubboKnowledgeBaseService dubboKnowledgeBaseService;

    @Override
    @SentinelResource("kb.get")
    public List<KnowledgeDTO> getKnowledgesByOrgId(Long orgId) {
        List<KnowledgeBaseDTO> knowledgeBases = dubboKnowledgeBaseService.getKnowledgeBaseByOrganizationId(orgId);
        return Lists.createWhenNull(knowledgeBases).stream().map(knowledgeBaseDTO -> {
            KnowledgeDTO knowledgeDTO = new KnowledgeDTO();
            BeanUtils.copyProperties(knowledgeBaseDTO, knowledgeDTO);
            return knowledgeDTO;
        }).toList();
    }

    @Override
    public KnowledgeDTO getKnowledgeById(Long knowledgeId) {
        Assert.notNull(knowledgeId, "knowledgeId is null");
        KnowledgeDTO knowledgeDTO = new KnowledgeDTO();
        BeanUtils.copyProperties(dubboKnowledgeBaseService.getKnowledgeBase(knowledgeId), knowledgeDTO);
        return knowledgeDTO;
    }

    @Override
    @SentinelResource("kb.f.get")
    public List<KnowledgeFileDTO> getKnowledgeFilesByKnowledgeId(Long knowledgeId) {
        List<KnowledgeBaseFileDTO> knowledgeBaseFiles =
                dubboKnowledgeBaseService.getKnowledgeBaseFileByKnowledgeBaseId(knowledgeId);
        return Lists.createWhenNull(knowledgeBaseFiles).stream().map(knowledgeBaseFileDTO -> {
            KnowledgeFileDTO knowledgeFileDTO = new KnowledgeFileDTO();
            BeanUtils.copyProperties(knowledgeBaseFileDTO, knowledgeFileDTO);
            return knowledgeFileDTO;
        }).toList();
    }

    @Override
    @SentinelResource("kb.create")
    public KnowledgeDTO createKnowledge(KnowledgeDTO knowledge) {
        Assert.notNull(knowledge, "knowledge must not be null");
        // create
        KnowledgeBaseDTO knowledgeBaseDTO = new KnowledgeBaseDTO();
        BeanUtils.copyProperties(knowledge, knowledgeBaseDTO);
        // convert
        KnowledgeDTO knowledgeDTO = new KnowledgeDTO();
        BeanUtils.copyProperties(dubboKnowledgeBaseService.createKnowledgeBase(knowledgeBaseDTO), knowledgeDTO);
        return knowledgeDTO;
    }

    @Override
    @SentinelResource("kb.f.create")
    public List<KnowledgeFileDTO> createKnowledgeFile(List<KnowledgeFileDTO> knowledgeFiles) {
        Assert.notNull(knowledgeFiles, "knowledgeFiles must not be null");
        // batch create
        List<KnowledgeBaseFileDTO> collect = knowledgeFiles.stream().map(kf -> {
            KnowledgeBaseFileDTO knowledgeBaseFileDTO = new KnowledgeBaseFileDTO();
            BeanUtils.copyProperties(kf, knowledgeBaseFileDTO);
            return knowledgeBaseFileDTO;
        }).toList();
        List<KnowledgeBaseFileDTO> knowledgeBaseFileDTOS =
                dubboKnowledgeBaseService.batchCreateKnowledgeBaseFiles(collect);
        // return create result
        return Lists.createWhenNull(knowledgeBaseFileDTOS).stream().map(kbf -> {
            KnowledgeFileDTO knowledgeFileDTO = new KnowledgeFileDTO();
            BeanUtils.copyProperties(kbf, knowledgeFileDTO);
            return knowledgeFileDTO;
        }).toList();
    }

    @Override
    @SentinelResource("kb.update")
    public boolean updateKnowledge(KnowledgeDTO knowledge) {
        Assert.notNull(knowledge, "knowledge must not be null");
        KnowledgeBaseDTO knowledgeBaseDTO = new KnowledgeBaseDTO();
        BeanUtils.copyProperties(knowledge, knowledgeBaseDTO);
        return dubboKnowledgeBaseService.updateKnowledgeBase(knowledgeBaseDTO);
    }

    @Override
    @SentinelResource("kb.delete")
    public boolean deleteKnowledge(Long knowledgeId) {
        Assert.notNull(knowledgeId, "knowledgeId must not be null");
        return dubboKnowledgeBaseService.deleteKnowledgeBase(knowledgeId);
    }

    @Override
    @SentinelResource("kb.f.delete")
    public boolean deleteKnowledgeFile(List<Long> knowledgeFileIds) {
        Assert.notNull(knowledgeFileIds, "knowledgeFileIds must not be null");
        return dubboKnowledgeBaseService.batchDeleteKnowledgeBaseFiles(knowledgeFileIds);
    }
}
