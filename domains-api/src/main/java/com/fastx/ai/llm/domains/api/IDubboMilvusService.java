package com.fastx.ai.llm.domains.api;

import java.util.List;
import java.util.Map;

/**
 * @author stark
 */
public interface IDubboMilvusService {

    /**
     * add vector
     * @param scalars key-value map
     * @param vectors vector data
     * @param collectionName collection name
     * @param partitionName partition name
     */
    void addVector(
            Map<String, Object> scalars,
            Map<String, List<Float>> vectors,
            String collectionName,
            String partitionName);

    /**
     * upsert vector
     * @param scalars key-value map
     * @param vectors vector data
     * @param collectionName collection name
     * @param partitionName partition name
     */
    void upsertVector(
            Map<String, Object> scalars,
            Map<String, List<Float>> vectors,
            String collectionName,
            String partitionName);

    /**
     * delete vector
     * @param ids id list
     * @param collectionName collection name
     * @param partitionName partition name
     */
    void deleteVector(
            List<Long> ids,
            String collectionName,
            String partitionName);

    /**
     * search vector
     * @param ids id list
     * @param annField ann field name
     * @param query query vector
     * @param limit top k
     * @param searchParams search params
     *                     (size > 1) -> HybridSearch
     * @param outputFields output fields
     * @param filter filter
     * @param groupByField group by field
     * @param groupSize group size
     * @param ranker WeightedRanker / RRFRanker
     * @param collectionName collection name
     * @param partitionName partition name
     * @return search result
     */
    List<Map<String, Object>> searchVector(
            List<Long> ids,
            String annField,
            List<Float> query,
            List<Map<String, Object>> searchParams,
            List<String> outputFields,
            String filter,
            String groupByField,
            Integer groupSize,
            Integer limit,
            String ranker,
            String collectionName,
            String partitionName);
}
