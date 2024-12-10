package com.fastx.ai.llm.platform.tool.spi;

import java.io.OutputStream;

/**
 * @author stark
 */
public interface IPlatformToolInput {

    /**
     * get JSON data for input
     * @return return JSON
     */
    String getData();

    /**
     * get config data
     * @return config JSON
     */
    String getConfig();
    /**
     * stream
     * @return stream
     */
    OutputStream getStream();

}
