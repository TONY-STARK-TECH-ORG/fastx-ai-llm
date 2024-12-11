package com.fastx.ai.llm.domains.service;

import io.milvus.v2.service.vector.request.DeleteReq;
import io.milvus.v2.service.vector.request.HybridSearchReq;
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.request.UpsertReq;

import java.util.List;
import java.util.Map;

/**
 * @author stark
 */
public interface IMilvusService {

    /**
     * add new data
     * @param upsertReq upsert req
     * @return upsert cnt
     */
    long upsert(UpsertReq upsertReq);

    /**
     * delete batch ids
     * @param deleteReq req
     * @return delete cnt
     */
    long delete(DeleteReq deleteReq);

    /**
     * query data
     *
     * @param searchReq req
     * @return search result
     */
    List<Map<String, Object>> search(SearchReq searchReq);

    /**
     * hybrid query
     * @param hybridSearchReq req
     * @return hybrid search result
     */
    List<Map<String, Object>> hybridSearch(HybridSearchReq hybridSearchReq);
}
