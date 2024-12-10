package com.fastx.ai.llm.domains.service.impl;

import com.fastx.ai.llm.domains.config.MilvusConfig;
import com.fastx.ai.llm.domains.service.IMilvusService;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author stark
 */
@Slf4j
@Service
public class MilvusServiceImpl implements IMilvusService {

    @Autowired
    MilvusConfig milvusConfig;

    @Override
    public void createCollection(CreateCollectionReq createCollectionReq) {
        // create a client
        String clientName = "fastx-ai-llm-client-" + UUID.randomUUID();
        MilvusClientV2 client = milvusConfig.getPool().getClient(clientName);
        try {
            // exec commands
            client.createCollection(createCollectionReq);
            log.info("collection create success");
        } catch (Exception e) {
            log.error("create collection failed", e);
        } finally {
            // ignored
            milvusConfig.getPool().returnClient(clientName, client);
        }
    }

}
