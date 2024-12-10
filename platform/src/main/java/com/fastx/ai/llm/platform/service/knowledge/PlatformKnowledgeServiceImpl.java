package com.fastx.ai.llm.platform.service.knowledge;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.api.IDubboKnowledgeBaseService;
import com.fastx.ai.llm.domains.dto.KnowledgeBaseDTO;
import com.fastx.ai.llm.domains.dto.KnowledgeBaseFileDTO;
import com.fastx.ai.llm.platform.api.IPlatformKnowledgeService;
import com.fastx.ai.llm.platform.dto.KnowledgeDTO;
import com.fastx.ai.llm.platform.dto.KnowledgeFileDTO;
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
        return knowledgeBases.stream().map(knowledgeBaseDTO -> {
            KnowledgeDTO knowledgeDTO = new KnowledgeDTO();
            BeanUtils.copyProperties(knowledgeBaseDTO, knowledgeDTO);
            return knowledgeDTO;
        }).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("kb.f.get")
    public List<KnowledgeFileDTO> getKnowledgeFilesByKnowledgeId(Long knowledgeId) {
        List<KnowledgeBaseFileDTO> knowledgeBaseFiles =
                dubboKnowledgeBaseService.getKnowledgeBaseFileByKnowledgeBaseId(knowledgeId);
        return knowledgeBaseFiles.stream().map(knowledgeBaseFileDTO -> {
            KnowledgeFileDTO knowledgeFileDTO = new KnowledgeFileDTO();
            BeanUtils.copyProperties(knowledgeBaseFileDTO, knowledgeFileDTO);
            return knowledgeFileDTO;
        }).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("kb.create")
    public KnowledgeDTO createKnowledge(KnowledgeDTO knowledge) {
        KnowledgeBaseDTO knowledgeBaseDTO = new KnowledgeBaseDTO();

        BeanUtils.copyProperties(knowledge, knowledgeBaseDTO);
        knowledgeBaseDTO = dubboKnowledgeBaseService.createKnowledgeBase(knowledgeBaseDTO);

        KnowledgeDTO knowledgeDTO = new KnowledgeDTO();
        BeanUtils.copyProperties(knowledgeBaseDTO, knowledgeDTO);
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
        }).collect(Collectors.toList());
        List<KnowledgeBaseFileDTO> knowledgeBaseFileDTOS =
                dubboKnowledgeBaseService.batchCreateKnowledgeBaseFiles(collect);
        // return create result
        return knowledgeBaseFileDTOS.stream().map(kbf -> {
            KnowledgeFileDTO knowledgeFileDTO = new KnowledgeFileDTO();
            BeanUtils.copyProperties(kbf, knowledgeFileDTO);
            return knowledgeFileDTO;
        }).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("kb.update")
    public boolean updateKnowledge(KnowledgeDTO knowledge) {
        KnowledgeBaseDTO knowledgeBaseDTO = new KnowledgeBaseDTO();
        BeanUtils.copyProperties(knowledge, knowledgeBaseDTO);
        return dubboKnowledgeBaseService.updateKnowledgeBase(knowledgeBaseDTO);
    }

    @Override
    @SentinelResource("kb.delete")
    public boolean deleteKnowledge(Long knowledgeId) {
        return dubboKnowledgeBaseService.deleteKnowledgeBase(knowledgeId);
    }

    @Override
    @SentinelResource("kb.f.delete")
    public boolean deleteKnowledgeFile(List<Long> knowledgeFileIds) {
        return dubboKnowledgeBaseService.batchDeleteKnowledgeBaseFiles(knowledgeFileIds);
    }
}
