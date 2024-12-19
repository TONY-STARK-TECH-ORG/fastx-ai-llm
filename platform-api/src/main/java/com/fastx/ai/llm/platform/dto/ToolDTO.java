package com.fastx.ai.llm.platform.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public class ToolDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String icon;

    private String name;

    private String description;

    private String code;

    private String version;

    private String status;

    /**
     * tool config: input, output ...
     */
    private String prototype;

    private String author;

    private String type;

    private Boolean needConfig;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrototype() {
        return prototype;
    }

    public void setPrototype(String prototype) {
        this.prototype = prototype;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getNeedConfig() {
        return needConfig;
    }

    public void setNeedConfig(Boolean needConfig) {
        this.needConfig = needConfig;
    }
}
