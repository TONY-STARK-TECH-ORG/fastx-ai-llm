package com.fastx.ai.llm.domains.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author stark
 */
@Data
@Component
@ConfigurationProperties(prefix = "domain.milvus")
public class MilvusProperties {

    private String host;
    private String token;
    private String username;
    private String password;

}
