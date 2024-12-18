package com.fastx.ai.llm.platform.tool.llm.function.imagine.types;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author stark
 */
public class OpenAIImagineResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<String> urls;

    public static OpenAIImagineResponse of(List<String> urls) {
        OpenAIImagineResponse r = new OpenAIImagineResponse();
        r.setUrls(urls);
        return r;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
