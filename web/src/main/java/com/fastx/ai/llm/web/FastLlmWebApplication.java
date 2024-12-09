package com.fastx.ai.llm.web;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author stark
 */
@EnableDubbo
@SpringBootApplication(scanBasePackages = "com.fastx.ai.llm")
public class FastLlmWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastLlmWebApplication.class, args);
    }

}
