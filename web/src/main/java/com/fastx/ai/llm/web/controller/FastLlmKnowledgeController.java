package com.fastx.ai.llm.web.controller;

import com.fastx.ai.llm.platform.api.IPlatformKnowledgeService;
import com.fastx.ai.llm.platform.dto.KnowledgeDTO;
import com.fastx.ai.llm.platform.dto.KnowledgeFileDTO;
import com.fastx.ai.llm.web.controller.entity.Response;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author stark
 */
@RestController
@RequestMapping("/knowledge")
public class FastLlmKnowledgeController {

    @DubboReference
    IPlatformKnowledgeService platformKnowledgeService;

    @PostMapping("/create")
    public Response<KnowledgeDTO> create(@RequestBody KnowledgeDTO knowledgeDTO) {
        return Response.success(platformKnowledgeService.createKnowledge(knowledgeDTO));
    }

    @PostMapping("/update")
    public Response<Boolean> update(@RequestBody KnowledgeDTO knowledgeDTO) {
        return Response.success(platformKnowledgeService.updateKnowledge(knowledgeDTO));
    }

    @GetMapping("/list")
    public Response<List<KnowledgeDTO>> listKnowledges(Long orgId) {
        return Response.success(
                platformKnowledgeService.getKnowledgesByOrgId(orgId)
        );
    }

    @PostMapping("/file/create")
    public Response<List<KnowledgeFileDTO>> createFile(@RequestBody List<KnowledgeFileDTO> knowledgeFiles) {
        return Response.success(
                platformKnowledgeService.createKnowledgeFile(knowledgeFiles)
        );
    }

    @GetMapping("/file/list")
    public Response<List<KnowledgeFileDTO>> listFile(Long knowledgeId) {
        return Response.success(
                platformKnowledgeService.getKnowledgeFilesByKnowledgeId(knowledgeId)
        );
    }

    @PostMapping("delete")
    public Response<Boolean> delete(@RequestBody KnowledgeDTO knowledge) {
        return Response.success(
                platformKnowledgeService.deleteKnowledge(knowledge.getId())
        );
    }

    @PostMapping("/file/delete")
    public Response<Boolean> deleteFile(@RequestBody List<KnowledgeFileDTO> knowledgeFiles) {
        Assert.notEmpty(knowledgeFiles, "knowledgeFiles is empty");
        return Response.success(
                platformKnowledgeService.deleteKnowledgeFile(
                        knowledgeFiles.stream().map(KnowledgeFileDTO::getId).collect(Collectors.toList()))
        );
    }

}
