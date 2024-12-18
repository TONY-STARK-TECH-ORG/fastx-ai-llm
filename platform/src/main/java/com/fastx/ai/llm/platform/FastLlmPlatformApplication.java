package com.fastx.ai.llm.platform;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author stark
 */
@EnableDubbo
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.fastx.ai.llm")
public class FastLlmPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastLlmPlatformApplication.class, args);
    }

}
