package com.fastx.ai.llm.domains.service.impl;

import com.fastx.ai.llm.domains.config.MilvusConfig;
import com.fastx.ai.llm.domains.service.IMilvusService;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.service.vector.request.DeleteReq;
import io.milvus.v2.service.vector.request.HybridSearchReq;
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.request.UpsertReq;
import io.milvus.v2.service.vector.response.DeleteResp;
import io.milvus.v2.service.vector.response.SearchResp;
import io.milvus.v2.service.vector.response.UpsertResp;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author stark
 */
@Slf4j
@Service
public class MilvusServiceImpl implements IMilvusService {

    @Autowired
    MilvusConfig milvusConfig;

    @Override
    public long upsert(UpsertReq upsertReq) {
        // create a client
        String clientName = "fastx-ai-llm-client-" + UUID.randomUUID();
        MilvusClientV2 client = milvusConfig.getPool().getClient(clientName);
        try {
            // exec commands
            UpsertResp upsertResp = client.upsert(upsertReq);
            log.info("upsert data to collection success, upsertCnt: {}", upsertResp.getUpsertCnt());
            return upsertResp.getUpsertCnt();
        } catch (Exception e) {
            log.error("upsert data to collection failed", e);
        } finally {
            // ignored
            milvusConfig.getPool().returnClient(clientName, client);
        }
        return -1;
    }

    @Override
    public long delete(DeleteReq deleteReq) {
        // create a client
        String clientName = "fastx-ai-llm-client-" + UUID.randomUUID();
        MilvusClientV2 client = milvusConfig.getPool().getClient(clientName);
        try {
            // exec commands
            DeleteResp deleteResp = client.delete(deleteReq);
            log.info("delete data success, upsertCnt: {}", deleteResp.getDeleteCnt());
            return deleteResp.getDeleteCnt();
        } catch (Exception e) {
            log.error("upsert data to collection failed", e);
        } finally {
            // ignored
            milvusConfig.getPool().returnClient(clientName, client);
        }
        return -1;
    }

    @Override
    public List<Map<String, Object>> search(SearchReq searchReq) {
        // create a client
        String clientName = "fastx-ai-llm-client-" + UUID.randomUUID();
        MilvusClientV2 client = milvusConfig.getPool().getClient(clientName);
        try {
            // exec commands
            SearchResp search = client.search(searchReq);
            return getResultMaps(search);
        } catch (Exception e) {
            log.error("upsert data to collection failed", e);
        } finally {
            // ignored
            milvusConfig.getPool().returnClient(clientName, client);
        }
        return List.of();
    }

    @Override
    public List<Map<String, Object>> hybridSearch(HybridSearchReq hybridSearchReq) {
        // create a client
        String clientName = "fastx-ai-llm-client-" + UUID.randomUUID();
        MilvusClientV2 client = milvusConfig.getPool().getClient(clientName);
        try {
            // exec commands
            SearchResp search = client.hybridSearch(hybridSearchReq);
            return getResultMaps(search);
        } catch (Exception e) {
            log.error("upsert data to collection failed", e);
        } finally {
            // ignored
            milvusConfig.getPool().returnClient(clientName, client);
        }
        return List.of();
    }

    @NotNull
    private static List<Map<String, Object>> getResultMaps(SearchResp search) {
        List<Map<String, Object>> searchResultMap = new ArrayList<>();
        for (List<SearchResp.SearchResult> searchResult : search.getSearchResults()) {
            for (SearchResp.SearchResult result : searchResult) {
                Map<String, Object> data = result.getEntity();
                Object id = result.getId();
                Float score = result.getScore();

                Map<String, Object> element = new HashMap<>();
                element.put("id", id);
                element.put("score", score);
                element.put("data", data);
                searchResultMap.add(element);
            }
        }
        return searchResultMap;
    }
}
