package com.fastx.ai.llm.web.controller;

import com.fastx.ai.llm.web.controller.entity.Response;
import com.fastx.ai.llm.web.controller.entity.WorkspaceFileUploadRequest;
import com.fastx.ai.llm.web.service.OssUploadService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/uploadToWorkSpace")
    @ResponseBody
    public Response<String> handleFileUpload(@RequestParam("workspaceId") Long workspaceId, @RequestParam("file") MultipartFile file) {
        try {
            Assert.notNull(workspaceId, "workspaceId can not be null");
            return Response.success(
                    ossUploadService.upload(file, String.valueOf(workspaceId))
            );
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
