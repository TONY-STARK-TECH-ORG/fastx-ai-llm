package com.fastx.ai.llm.platform.service.milvus;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.api.IDubboMilvusService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;
import java.util.Map;

/**
 * @author stark
 */
@DubboService
public class PlatformMilvusService {

    @DubboReference
    IDubboMilvusService milvusService;

    @SentinelResource("milvus.upsert")
    public long add(
            Map<String, Object> scalars,
            Map<String, List<Float>> vectors,
            String collectionName) {
        return milvusService.add(scalars, vectors, collectionName);
    }

    @SentinelResource("milvus.upsert")
    public long upsert(
            Map<String, Object> scalars,
            Map<String, List<Float>> vectors,
            String collectionName) {
        return milvusService.upsert(scalars, vectors, collectionName);
    }

    @SentinelResource("milvus.delete")
    public long delete(
            List<Object> ids,
            String collectionName,
            String filter) {
        return milvusService.delete(ids, collectionName, filter);
    }

    /**
     * Search vectors in Milvus.
     *
     * @param collectionName      The name of the collection to search.
     * @param annsField           The field name that contains the vectors to be searched.
     * @param filter              A filter expression to apply to the search results (e.g., "age > 30").
     * @param outputFields        A list of field names to include in the search output.
     * @param data                A list of query vectors (can be one or multiple vectors).
     * @param offset              The offset of the first entry to return (for pagination purposes).
     * @param limit               The maximum number of entries to return (for pagination purposes).
     * @param searchParams        The JSON string of search parameters.
     * @param guaranteeTimestamp  A timestamp to ensure consistency level of the search (usually used with gracefulTime).
     * @param gracefulTime        Additional time in milliseconds to consider for consistency.
     * @param groupByFieldName    Field name to group the results by.
     * @return                    A list of maps containing the search results.
     */
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
            String groupByFieldName
    ) {
        return milvusService.search(
                collectionName,
                annsField,
                filter,
                outputFields,
                data,
                offset,
                limit,
                searchParams,
                guaranteeTimestamp,
                gracefulTime,
                groupByFieldName
        );
    }

    /**
     * Perform a hybrid search that combines multiple search criteria or strategies.
     *
     * @param collectionName  The name of the collection to search.
     * @param databaseName    The name of the database.
     * @param searchRequests  A list of search requests, each defined by its own parameters.
     *                        for build annRequests.
     *                        need: vector (List with Float generic), metricType, topK, vectorFieldName, params and expr.
     * @param ranker          The ranking strategy to use for sorting the search results. L2/IP
     * @param RRFRankerK      rrf ranker K
     * @param WeightRankerWeights weight ranker weights
     * @param topK            The number of top results to return.
     * @param outFields       A list of field names to include in the search output.
     * @return                A list of maps containing the search results.
     */
    @SentinelResource("milvus.search")
    public List<Map<String, Object>> hybridSearch(
            String collectionName,
            String databaseName,
            List<Map<String, Object>> searchRequests,
            String ranker,
            int RRFRankerK,
            List<Float> WeightRankerWeights,
            int topK,
            List<String> outFields
    ) {
        return milvusService.hybridSearch(
                collectionName, databaseName, searchRequests, ranker, RRFRankerK, WeightRankerWeights, topK, outFields);
    }

}
