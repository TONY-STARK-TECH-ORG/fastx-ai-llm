package com.fastx.ai.llm.domains.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
public class BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * identifier
     */
    private Long id;

    /**
     * data created time
     */
    private String createTime;

    /**
     * data updated time
     */
    private String updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
