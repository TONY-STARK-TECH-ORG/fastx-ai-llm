package com.fastx.ai.llm.domains.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.service.IMilvusService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author stark
 */
@DubboService
public class DubboMilvusServiceImpl implements IDubboMilvusService {

    @Autowired
    IMilvusService milvusService;

    @Override
    @SentinelResource("milvus.add")
    public void addVector(Map<String, Object> scalars, Map<String, List<Float>> vectors, String collectionName, String partitionName) {

    }

    @Override
    @SentinelResource("milvus.upsert")
    public void upsertVector(Map<String, Object> scalars, Map<String, List<Float>> vectors, String collectionName, String partitionName) {

    }

    @Override
    @SentinelResource("milvus.delete")
    public void deleteVector(List<Long> ids, String collectionName, String partitionName) {

    }

    @Override
    @SentinelResource("milvus.search")
    public List<Map<String, Object>> searchVector(List<Long> ids, String annField, List<Float> query, List<Map<String, Object>> searchParams, List<String> outputFields, String filter, String groupByField, Integer groupSize, Integer limit, String ranker, String collectionName, String partitionName) {
        return List.of();
    }
}
