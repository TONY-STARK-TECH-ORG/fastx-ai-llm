package com.fastx.ai.llm.domains.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
public class KnowledgeBaseFileDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long knowledgeBaseId;

    private String name;

    private String extension;

    private String downloadUrl;

    private String vecCollectionName;

    private String vecCollectionId;

    private String vecPartitionKey;

    private String vecPartitionValue;

    private String status;

    public Long getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public void setKnowledgeBaseId(Long knowledgeBaseId) {
        this.knowledgeBaseId = knowledgeBaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVecCollectionName() {
        return vecCollectionName;
    }

    public void setVecCollectionName(String vecCollectionName) {
        this.vecCollectionName = vecCollectionName;
    }

    public String getVecCollectionId() {
        return vecCollectionId;
    }

    public void setVecCollectionId(String vecCollectionId) {
        this.vecCollectionId = vecCollectionId;
    }

    public String getVecPartitionKey() {
        return vecPartitionKey;
    }

    public void setVecPartitionKey(String vecPartitionKey) {
        this.vecPartitionKey = vecPartitionKey;
    }

    public String getVecPartitionValue() {
        return vecPartitionValue;
    }

    public void setVecPartitionValue(String vecPartitionValue) {
        this.vecPartitionValue = vecPartitionValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
