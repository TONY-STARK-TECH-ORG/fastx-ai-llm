package com.fastx.ai.llm.domains.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.dto.KnowledgeBaseDTO;
import com.fastx.ai.llm.domains.dto.KnowledgeBaseFileDTO;
import com.fastx.ai.llm.domains.entity.KnowledgeBase;
import com.fastx.ai.llm.domains.entity.KnowledgeBaseFile;
import com.fastx.ai.llm.domains.service.IKnowledgeBaseFileService;
import com.fastx.ai.llm.domains.service.IKnowledgeBaseService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
@DubboService
public class DubboKnowledgeDubboBaseServiceImpl extends DubboBaseDomainService implements IDubboKnowledgeBaseService {

    @Autowired
    IKnowledgeBaseService knowledgeBaseService;

    @Autowired
    IKnowledgeBaseFileService knowledgeBaseFileService;

    @Override
    @SentinelResource("kb.create")
    public KnowledgeBaseDTO createKnowledgeBase(KnowledgeBaseDTO knowledgeBaseDTO) {
        isValidated(knowledgeBaseDTO);
        knowledgeBaseDTO.setStatus(IConstant.ACTIVE);

        KnowledgeBase kb = KnowledgeBase.of(knowledgeBaseDTO);
        Assert.isTrue(knowledgeBaseService.save(kb), "save knowledge failed");
        return kb.to();
    }

    @Override
    @SentinelResource("kb.update")
    public boolean updateKnowledgeBase(KnowledgeBaseDTO knowledgeBaseDTO) {
        isValidated(knowledgeBaseDTO);
        Assert.notNull(knowledgeBaseDTO.getId(), "knowledge id is null");
        KnowledgeBase kb = KnowledgeBase.of(knowledgeBaseDTO);
        return knowledgeBaseService.updateById(kb);
    }

    @Override
    @SentinelResource("kb.get")
    public KnowledgeBaseDTO getKnowledgeBase(Long id) {
        KnowledgeBase knowledgeBase = knowledgeBaseService.getById(id);
        Assert.notNull(knowledgeBase, "not found knowledge");
        return knowledgeBase.to();
    }

    @Override
    @SentinelResource("kb.get")
    public List<KnowledgeBaseDTO> getKnowledgeBaseByOrganizationId(Long organizationId) {
        Assert.notNull(organizationId, "organizationId is null");
        List<KnowledgeBase> knowledgeBases = knowledgeBaseService.getKnowledgeBaseByOrganizationId(organizationId);
        return knowledgeBases.stream().map(KnowledgeBase::to).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("kb.delete")
    public boolean deleteKnowledgeBase(Long id) {
        Assert.notNull(id, "id is null");
        Assert.isTrue(knowledgeBaseService.removeById(id), "delete knowledge failed");
        // remove all files;
        List<KnowledgeBaseFileDTO> files = getKnowledgeBaseFileByKnowledgeBaseId(id);
        if (CollectionUtils.isNotEmpty(files)) {
            Assert.isTrue(
                    knowledgeBaseFileService.removeByIds(
                            files.stream().map(KnowledgeBaseFileDTO::getId).collect(Collectors.toList())),
                    "delete knowledge files failed");
        }
        return true;
    }

    @Override
    @SentinelResource("kb.f.get")
    public KnowledgeBaseFileDTO getKnowledgeBaseFile(Long id) {
        KnowledgeBaseFile knowledgeBaseFile = knowledgeBaseFileService.getById(id);
        Assert.notNull(knowledgeBaseFile, "not found knowledge file");
        return knowledgeBaseFile.to();
    }

    @Override
    @SentinelResource("kb.f.get")
    public List<KnowledgeBaseFileDTO> getKnowledgeBaseFileByKnowledgeBaseId(Long knowledgeBaseId) {
        Assert.notNull(knowledgeBaseId, "knowledgeBaseId is null");
        List<KnowledgeBaseFile> knowledgeBaseFiles =
                knowledgeBaseFileService.getKnowledgeBaseFileByKnowledgeBaseId(knowledgeBaseId);
        return knowledgeBaseFiles.stream().map(KnowledgeBaseFile::to).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("kb.f.create")
    public List<KnowledgeBaseFileDTO> batchCreateKnowledgeBaseFiles(List<KnowledgeBaseFileDTO> knowledgeBaseFileDTOs) {
        Assert.notNull(knowledgeBaseFileDTOs, "knowledgeBaseFileDTOs is null");
        knowledgeBaseFileDTOs.forEach(this::isValidated);
        List<KnowledgeBaseFile> knowledgeBaseFiles = knowledgeBaseFileDTOs.stream().map(KnowledgeBaseFile::of).collect(Collectors.toList());
        Assert.isTrue(knowledgeBaseFileService.saveBatch(knowledgeBaseFiles), "save knowledge files failed");
        return knowledgeBaseFiles.stream().map(KnowledgeBaseFile::to).collect(Collectors.toList());
    }

    @Override
    @SentinelResource("kb.f.delete")
    public boolean deleteKnowledgeBaseFile(Long id) {
        Assert.notNull(id, "id is null");
        return knowledgeBaseFileService.removeById(id);
    }

    @Override
    @SentinelResource("kb.f.delete")
    public boolean batchDeleteKnowledgeBaseFiles(List<Long> ids) {
        Assert.notEmpty(ids, "ids is null");
        return knowledgeBaseFileService.removeByIds(ids);
    }

    private void isValidated(KnowledgeBaseDTO knowledgeBaseDTO) {
        Assert.notNull(knowledgeBaseDTO, "knowledgeBase must not be null");
        AssertUtil.notEmpty(knowledgeBaseDTO.getName(), "name must not be empty");
        AssertUtil.notEmpty(knowledgeBaseDTO.getDescription(), "description must not be empty");
        AssertUtil.notNull(knowledgeBaseDTO.getOrganizationId(), "organizationId must not be null");
    }

    private void isValidated(KnowledgeBaseFileDTO knowledgeBaseFileDTO) {
        Assert.notNull(knowledgeBaseFileDTO, "knowledgeBaseFile must not be null");
        Assert.notNull(knowledgeBaseFileDTO.getKnowledgeBaseId(), "knowledgeBaseId must not be null");
        AssertUtil.notEmpty(knowledgeBaseFileDTO.getName(), "name must not be empty");
        AssertUtil.notEmpty(knowledgeBaseFileDTO.getExtension(), "extension must not be empty");
        AssertUtil.notEmpty(knowledgeBaseFileDTO.getDownloadUrl(), "downloadUrl must not be empty");
        AssertUtil.notEmpty(knowledgeBaseFileDTO.getVecCollectionId(), "vecCollectionId must not be empty");
        AssertUtil.notEmpty(knowledgeBaseFileDTO.getVecCollectionName(), "vecCollectionName must not be empty");
        AssertUtil.notEmpty(knowledgeBaseFileDTO.getVecPartitionKey(), "vecPartitionKey must not be empty");
        AssertUtil.notEmpty(knowledgeBaseFileDTO.getVecPartitionValue(), "vecPartitionValue must not be empty");
    }
}
