package com.fastx.ai.llm.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author stark
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.oss")
public class OssUploadConfig {

    private String endpoint;
    private String bucket;
    private String region;
    private String accessKey;
    private String accessSecret;
    private String domain;

}
