package com.fastx.ai.llm.web;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author stark
 */
@EnableDubbo
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = "com.fastx.ai.llm")
public class FastLlmWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastLlmWebApplication.class, args);
    }

}
