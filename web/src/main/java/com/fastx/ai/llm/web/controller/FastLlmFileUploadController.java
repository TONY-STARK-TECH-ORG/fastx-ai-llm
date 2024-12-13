package com.fastx.ai.llm.web.controller;

import com.fastx.ai.llm.web.controller.entity.Response;
import com.fastx.ai.llm.web.controller.entity.WorkspaceFileUploadRequest;
import com.fastx.ai.llm.web.service.OssUploadService;
import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @PostMapping(value = "/uploadToWorkSpace", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Response<List<Map<String, String>>> handleFileUpload(
            @RequestParam String workspaceId,
            @RequestParam("files") MultipartFile[] files) {
        // allow files
        try {
            Assert.notNull(workspaceId, "workspaceId can not be null");
            List<Map<String, String>> uploaded = new ArrayList<>();
            for (MultipartFile file : files) {
                uploaded.add(
                        Map.of(
                                StringUtils.defaultIf(file.getOriginalFilename(), file.getName()),
                                ossUploadService.upload(file, workspaceId)
                        ))
                ;
            }
            return Response.success(uploaded);
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
