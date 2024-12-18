package com.fastx.ai.llm.platform.tool.llm.tool.api.types;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author stark
 */
public class HttpRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String url;
    private String method = "GET";
    private Map<String, String> headers = new HashMap<>();
    private Map<String, Object> body = new HashMap<>();
    private Integer timeout = 5000;
    private String mediaType = "application/json";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
