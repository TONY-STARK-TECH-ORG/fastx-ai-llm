package com.fastx.ai.llm.domains.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastx.ai.llm.domains.config.lock.RedisLock;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.dto.KnowledgeBaseDTO;
import com.fastx.ai.llm.domains.dto.KnowledgeBaseFileDTO;
import com.fastx.ai.llm.domains.dto.PageDTO;
import com.fastx.ai.llm.domains.entity.KnowledgeBase;
import com.fastx.ai.llm.domains.entity.KnowledgeBaseFile;
import com.fastx.ai.llm.domains.service.IKnowledgeBaseFileService;
import com.fastx.ai.llm.domains.service.IKnowledgeBaseService;
import com.fastx.ai.llm.domains.service.IOrganizationService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

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

    @Autowired
    IOrganizationService organizationService;

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
    public List<KnowledgeBaseDTO> getKnowledgeBaseByOrganizationId(Long orgId) {
        Assert.notNull(orgId, "orgId is null");
        List<KnowledgeBase> knowledgeBases = knowledgeBaseService.getKnowledgeBaseByOrganizationIds(
                List.of(orgId)
        );
        return knowledgeBases.stream().map(KnowledgeBase::to).toList();
    }

    @Override
    @SentinelResource("kb.delete")
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteKnowledgeBase(Long id) {
        Assert.notNull(id, "id is null");
        Assert.isTrue(knowledgeBaseService.removeById(id), "delete knowledge failed");
        // remove all files;
        Assert.isTrue(knowledgeBaseFileService.removeFilesByKnowledgeBaseId(id), "delete knowledge files failed");
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
        return knowledgeBaseFiles.stream().map(KnowledgeBaseFile::to).toList();
    }

    @Override
    @SentinelResource("kb.f.create")
    public List<KnowledgeBaseFileDTO> batchCreateKnowledgeBaseFiles(List<KnowledgeBaseFileDTO> knowledgeBaseFileDTOs) {
        Assert.notNull(knowledgeBaseFileDTOs, "knowledgeBaseFileDTOs is null");
        knowledgeBaseFileDTOs.forEach(this::isValidated);
        List<KnowledgeBaseFile> knowledgeBaseFiles = knowledgeBaseFileDTOs.stream().map(KnowledgeBaseFile::of).toList();
        Assert.isTrue(knowledgeBaseFileService.saveBatch(knowledgeBaseFiles), "save knowledge files failed");
        return knowledgeBaseFiles.stream().map(KnowledgeBaseFile::to).toList();
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

    @Override
    @SentinelResource("kb.f.get")
    public PageDTO<KnowledgeBaseFileDTO> getKnowledgeBaseByPage(Long page, Long size, String status) {
        Assert.notNull(status, "status is null");
        Assert.notNull(page, "page is null");
        Assert.notNull(size, "size is null");

        Page<KnowledgeBaseFile> pageDTO = knowledgeBaseFileService.getKnowledgeBaseByPage(page, size, status);
        return PageDTO.of(pageDTO.getCurrent(), pageDTO.getSize(), pageDTO.getTotal(),
                pageDTO.getRecords().stream().map(KnowledgeBaseFile::to).toList());
    }

    @Override
    @SentinelResource("kb.f.update")
    @RedisLock(key = "updateKnowledgeBaseLock::${#knowledgeBaseFileDTO.id}")
    public Boolean updateKnowledgeBaseFile(KnowledgeBaseFileDTO knowledgeBaseFileDTO) {
        isValidated(knowledgeBaseFileDTO);
        Assert.notNull(knowledgeBaseFileDTO.getId(), "id is null");
        KnowledgeBaseFile knowledgeBaseFile = KnowledgeBaseFile.of(knowledgeBaseFileDTO);
        return knowledgeBaseFileService.updateById(knowledgeBaseFile);
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
    }
}
