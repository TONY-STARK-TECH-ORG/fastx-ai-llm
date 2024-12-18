package com.fastx.ai.llm.platform.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author stark
 */
public class PlatformPageDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long page;
    private Long size;
    private Long total;

    private List<T> list;

    public PlatformPageDTO(Long page, Long size, Long total, List<T> list) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.list = list;
    }

    public static <T> PlatformPageDTO<T> of(Long page, Long size, Long total, List<T> list) {
        return new PlatformPageDTO<>(page, size, total, list);
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
