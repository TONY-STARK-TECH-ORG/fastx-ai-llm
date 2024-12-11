package com.fastx.ai.llm.domains.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.exception.DomainServiceException;
import com.fastx.ai.llm.domains.service.IMilvusService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rometools.utils.Lists;
import io.milvus.v2.common.ConsistencyLevel;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.vector.request.*;
import io.milvus.v2.service.vector.request.data.Float16Vec;
import io.milvus.v2.service.vector.request.ranker.RRFRanker;
import io.milvus.v2.service.vector.request.ranker.WeightedRanker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author stark
 */
@Slf4j
@DubboService
public class DubboMilvusServiceImpl implements IDubboMilvusService {

    @Autowired
    IMilvusService milvusService;

    @Override
    @SentinelResource("milvus.upsert")
    public long add(Map<String, Object> scalars, Map<String, List<Float>> vectors, String collectionName) {
        // call upsert!
        return upsert(scalars, vectors, collectionName);
    }

    @Override
    @SentinelResource("milvus.upsert")
    public long upsert(Map<String, Object> scalars, Map<String, List<Float>> vectors, String collectionName) {
        Assert.isTrue(StringUtils.isEmpty(collectionName), "must have collectionName!");
        // parse vectors
        JsonObject dataDict = new JsonObject();
        Gson gson = new Gson();
        for (Map.Entry<String, List<Float>> entry : vectors.entrySet()) {
            // dimension check, if not right, padding or truncate
            // text_content: 1536
            // image_content: 512
            // audio_content: 128
            // video_content: 1024
            if (entry.getKey().endsWith("_content")) {
                int dimension = getDimension(entry);

                List<Float> newVector = entry.getValue();
                if (entry.getValue().size() != dimension) {
                    log.warn("dimension not fit collection config, manual fix it! {}, origin size: {}",
                            entry.getKey(), entry.getValue().size());
                    // truncate or padding
                    if (newVector.size() > dimension) {
                        newVector = newVector.subList(0, dimension);
                    } else if (newVector.size() < dimension) {
                        for (int i = newVector.size(); i < dimension; i++) {
                            newVector.add(0f);
                        }
                    }
                }
                dataDict.add(entry.getKey(), gson.toJsonTree(newVector));
            }
        }
        for (Map.Entry<String, Object> entry: scalars.entrySet()) {
            dataDict.add(entry.getKey(), gson.toJsonTree(entry.getValue()));
        }
        UpsertReq req = UpsertReq.builder()
                .collectionName(collectionName)
                .data(List.of(dataDict))
                .build();
        return milvusService.upsert(req);
    }

    private static int getDimension(Map.Entry<String, List<Float>> entry) {
        int dimension = -1;
        if (entry.getKey().contains("image")) {
            dimension = 512;
        } else if (entry.getKey().contains("audio")) {
            dimension = 128;
        } else if (entry.getKey().contains("video")) {
            dimension = 1024;
        } else if (entry.getKey().contains("text")) {
            dimension = 1536;
        }

        if (dimension == -1) {
            throw new DomainServiceException("unknown vector key!");
        }
        return dimension;
    }

    @Override
    @SentinelResource("milvus.delete")
    public long delete(List<Object> ids, String collectionName, String filter) {
        Assert.isTrue(
                CollectionUtils.isEmpty(ids) && StringUtils.isEmpty(filter), "must provide at least one condition!"
        );
        DeleteReq req = DeleteReq.builder()
                .collectionName(collectionName)
                .filter(StringUtils.defaultIfBlank(filter, ""))
                .ids(Lists.emptyToNull(ids))
                .build();
        return milvusService.delete(req);

    }

    @Override
    @SentinelResource("milvus.search")
    public List<Map<String, Object>> search(
            String collectionName,
            String annsField,
            String filter,
            List<String> outputFields,
            List<Float> data,
            long offset,
            long limit,
            Map<String, Object> searchParams,
            long guaranteeTimestamp,
            long gracefulTime,
            String groupByFieldName) {
        return milvusService.search(SearchReq.builder()
                .collectionName(collectionName)
                .annsField(annsField)
                .filter(filter)
                .outputFields(Lists.emptyToNull(outputFields))
                .data(List.of(new Float16Vec(Lists.emptyToNull(data))))
                .offset(offset)
                .limit(limit)
                .searchParams(searchParams)
                .guaranteeTimestamp(guaranteeTimestamp)
                .gracefulTime(gracefulTime)
                // default set to EVENTUALLY
                .consistencyLevel(ConsistencyLevel.EVENTUALLY)
                .ignoreGrowing(true)
                .groupByFieldName(groupByFieldName)
                .build()
        );
    }

    @Override
    @SentinelResource("milvus.search")
    public List<Map<String, Object>> hybridSearch(
            String collectionName,
            String databaseName,
            List<Map<String, Object>> searchRequests,
            String ranker,
            int RRFRankerK,
            List<Float> WeightRankerWeights,
            int topK,
            List<String> outFields) {
        List<AnnSearchReq> annSearchReqList = new ArrayList<>();
        for (Map<String, Object> searchRequest : searchRequests) {
            IndexParam.MetricType MT;
            if ("L2".equals(MapUtils.getString(searchRequest, "metricType", "L2"))) {
                MT = IndexParam.MetricType.L2;
            } else {
                MT = IndexParam.MetricType.IP;
            }
            List<Float> vec = List.of();
            if (Objects.nonNull(searchRequest.get("vector"))) {
                Optional<List<Float>> optionalVec = Optional.ofNullable(searchRequest.get("vector"))
                        .filter(List.class::isInstance)
                        .map(List.class::cast);

                if (optionalVec.isPresent()) {
                    vec = optionalVec.get();
                }
            }
            AnnSearchReq req = AnnSearchReq.builder()
                    .topK(MapUtils.getInteger(searchRequest, "topK", 10))
                    .vectorFieldName(MapUtils.getString(searchRequest, "vectorFieldName", "vector"))
                    .params(MapUtils.getString(searchRequest, "params", ""))
                    .expr(MapUtils.getString(searchRequest, "expr", ""))
                    .metricType(MT)
                    .vectors(List.of(new Float16Vec(vec)))
                    .build();
            annSearchReqList.add(req);
        }
        HybridSearchReq hybridSearchReq = HybridSearchReq.builder()
                .collectionName(collectionName)
                .databaseName(databaseName)
                .searchRequests(annSearchReqList)
                .ranker("RRF".equals(ranker) ? new RRFRanker(RRFRankerK) : new WeightedRanker(WeightRankerWeights))
                .topK(topK)
                .outFields(outFields)
                .build();
        return milvusService.hybridSearch(hybridSearchReq);
    }
}
