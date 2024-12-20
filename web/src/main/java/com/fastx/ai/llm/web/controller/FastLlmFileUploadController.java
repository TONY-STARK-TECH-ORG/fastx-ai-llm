package com.fastx.ai.llm.web.controller;

import com.fastx.ai.llm.platform.api.IPlatformKnowledgeService;
import com.fastx.ai.llm.platform.dto.KnowledgeDTO;
import com.fastx.ai.llm.platform.dto.KnowledgeFileDTO;
import com.fastx.ai.llm.web.controller.entity.Response;
import com.fastx.ai.llm.web.controller.entity.WorkspaceFileUploadRequest;
import com.fastx.ai.llm.web.service.OssUploadService;
import com.rometools.utils.Lists;
import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author stark
 */
@Controller
@RequestMapping("/file")
public class FastLlmFileUploadController {

    @Autowired
    OssUploadService ossUploadService;

    @Autowired
    IPlatformKnowledgeService platformKnowledgeService;

    @PostMapping(value = "/uploadToWorkSpace", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Response<List<KnowledgeFileDTO>> handleFileUpload(
            @RequestParam Long workspaceId,
            @RequestParam("file") MultipartFile[] files) {
        // validate kb
        KnowledgeDTO knowledgeById = platformKnowledgeService.getKnowledgeById(workspaceId);
        Assert.notNull(knowledgeById, "workspace is not exist");
        // allow files
        try {
            Assert.notNull(workspaceId, "workspaceId can not be null");
            List<KnowledgeFileDTO> knowledgeFiles = null;
            for (MultipartFile file : files) {
                String fileName = StringUtils.defaultIf(file.getOriginalFilename(), file.getName());
                String upload = ossUploadService.upload(file, String.valueOf(workspaceId));
                KnowledgeFileDTO fileDTO = new KnowledgeFileDTO();
                fileDTO.setName(fileName);
                // @TODO (stark) need file extension need pdf, doc, txt.
                // now this has a bug: store the full name for this file.
                fileDTO.setExtension(fileName.contains(".") ? StringUtils.defaultIf(fileName.substring(fileName.indexOf(".") + 1), "-") : "-");
                fileDTO.setDownloadUrl(upload);
                fileDTO.setKnowledgeBaseId(workspaceId);
                knowledgeFiles = platformKnowledgeService.createKnowledgeFile(List.of(fileDTO));
            }
            return Response.success(Lists.createWhenNull(knowledgeFiles));
        } catch (Exception e) {
            return Response.error("upload meet exception" + e.getMessage());
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public Response<List<Map<String, Object>>> list(@RequestBody WorkspaceFileUploadRequest uploadRequest) {
        Assert.notNull(uploadRequest.getWorkspaceId(), "workspaceId can not be null");
        List<Map<String, Object>> fileList = ossUploadService.list(String.valueOf(uploadRequest.getWorkspaceId()));
        return Response.success(fileList);
    }
}
