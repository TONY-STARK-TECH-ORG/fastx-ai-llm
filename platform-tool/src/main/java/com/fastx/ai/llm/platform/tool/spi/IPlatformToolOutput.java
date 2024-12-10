package com.fastx.ai.llm.platform.tool.spi;

/**
 * @author stark
 */
public interface IPlatformToolOutput {

    /**
     * get JSON data for input
     * @return return JSON
     */
    String getData();

    /**
     * if with error
     * @return err message
     */
    String getError();


}
