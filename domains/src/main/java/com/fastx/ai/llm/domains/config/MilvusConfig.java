package com.fastx.ai.llm.domains.config;

import io.milvus.pool.MilvusClientV2Pool;
import io.milvus.pool.PoolConfig;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author stark
 */
@Configuration
public class MilvusConfig {

    @Autowired
    MilvusProperties properties;

    @Getter
    private MilvusClientV2Pool pool;

    @Getter
    private MilvusClientV2 systemClientV2;

    @PostConstruct
    public void init() throws ClassNotFoundException, NoSuchMethodException {
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(properties.getHost())
                .token(properties.getToken())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .build();
        // pool config
        PoolConfig poolConfig = PoolConfig.builder()
                .maxIdlePerKey(10)
                .maxTotalPerKey(20)
                .maxTotal(100)
                .maxBlockWaitDuration(Duration.ofSeconds(5L))
                .minEvictableIdleDuration(Duration.ofSeconds(10L))
                .build();
        // initialize milvus pool.
        pool = new MilvusClientV2Pool(poolConfig, connectConfig);
        // initialize system default client.
        systemClientV2 = pool.getClient("fastx-ai-llm-client");
    }

}
