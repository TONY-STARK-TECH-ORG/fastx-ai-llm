package com.fastx.ai.llm.platform.tool.spi;

import java.io.OutputStream;

/**
 * @author stark
 */
public interface IPlatformToolInput {

    /**
     * set JSON data
     * @param data d
     */
    void setData(String data);

    /**
     * get JSON data for input
     * @return return JSON
     */
    String getData();

    /**
     * set config data
     * @param config c
     */
    void setConfig(String config);

    /**
     * get config data
     * @return config JSON
     */
    String getConfig();


    /**
     * stream
     * @param stream s
     */
    void setStream(OutputStream stream);

    /**
     * stream
     * @return stream
     */
    OutputStream getStream();

}
