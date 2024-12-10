package com.fastx.ai.llm.domains.service;

import io.milvus.v2.service.collection.request.CreateCollectionReq;

/**
 * @author stark
 */
public interface IMilvusService {

    /**
     * add new collection to milvus
     * @param createCollectionReq collection req
     */
    void createCollection(CreateCollectionReq createCollectionReq);

}
