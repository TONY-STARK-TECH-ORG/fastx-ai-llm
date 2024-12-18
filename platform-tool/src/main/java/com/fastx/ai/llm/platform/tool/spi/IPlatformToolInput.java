package com.fastx.ai.llm.platform.tool.spi;

import java.io.OutputStream;

/**
 * @author stark
 */
public interface IPlatformToolInput {

    /**
     * set JSON data
     * @param inputs d
     */
    void setInputs(String inputs);

    /**
     * get JSON data for input
     * @return return JSON
     */
    String getInputs();

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
