package com.fastx.ai.llm.web.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.ObjectListing;
import com.aliyuncs.exceptions.ClientException;
import com.fastx.ai.llm.web.config.OssUploadConfig;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author stark
 */
@Service
public class OssUploadService {

    @Autowired
    OssUploadConfig ossUploadConfig;

    private OSS ossClient;

    @PostConstruct
    public void init() throws ClientException {
        // create oss client
        DefaultCredentialProvider credentialProvider = CredentialsProviderFactory.newDefaultCredentialProvider(
                ossUploadConfig.getAccessKey(), ossUploadConfig.getAccessSecret()
        );
        ossClient = OSSClientBuilder.create()
                .endpoint(ossUploadConfig.getEndpoint())
                .credentialsProvider(credentialProvider)
                .region(ossUploadConfig.getRegion())
                .build();
    }

    /**
     * upload single file to oss
     * @param file front send file
     * @return fileName: URL
     */
    public String upload(MultipartFile file, String folderName) throws IOException {
        String fileName = "fastx-ai-llm/" + folderName + "/" +
                StringUtils.defaultIfBlank(file.getOriginalFilename(), file.getName());
        ossClient.putObject(ossUploadConfig.getBucket(), fileName, new ByteArrayInputStream(file.getBytes()));
        return ossUploadConfig.getDomain() + "/" + fileName;
    }

    /**
     * list all files in oss bucket
     * @param folderName folder name
     * @return fileName: URL
     */
    public List<Map<String, Object>> list(String folderName) {
        // build request
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(ossUploadConfig.getBucket());
        listObjectsRequest.setPrefix("fastx-ai-llm/" + folderName + "/");
        listObjectsRequest.setMaxKeys(1000);
        // list
        ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
        return objectListing.getObjectSummaries().stream().map(s -> {
            String key = s.getKey();
            long size = s.getSize();
            String type = s.getType();
            Date lastModified = s.getLastModified();

            Map<String, Object> map = new HashMap<>(3);
            map.put("url", ossUploadConfig.getDomain() + "/" + key);
            map.put("size", size);
            map.put("type", type);
            map.put("updateTime", DateFormatUtils.format(lastModified, "yyyy-MM-dd HH:mm:ss"));
            return map;
        }).toList();
    }

}
