package com.fastx.ai.llm.domains;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author stark
 */
@EnableDubbo
@EnableCaching

// used for clean db in fixed time.
@EnableScheduling

@EnableTransactionManagement
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = "com.fastx.ai.llm")
public class FastLlmDomainApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastLlmDomainApplication.class, args);
    }

}
