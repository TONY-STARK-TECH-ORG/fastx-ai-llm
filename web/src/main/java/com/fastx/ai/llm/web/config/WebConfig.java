package com.fastx.ai.llm.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author stark
 */
@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // 匹配所有路径
                        .allowedOrigins("http://localhost:5173", "http://localhost")  // 允许来自特定域的请求
                        .allowedMethods("*")  // 允许的HTTP方法
                        .allowedHeaders("*")  // 允许的请求头
                        .allowCredentials(true);  // 允许发送凭据
            }
        };
    }
}
