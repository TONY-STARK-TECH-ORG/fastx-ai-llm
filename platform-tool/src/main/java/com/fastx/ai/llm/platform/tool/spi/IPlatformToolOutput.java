package com.fastx.ai.llm.platform.tool.spi;

import org.apache.commons.lang3.StringUtils;

/**
 * @author stark
 */
public interface IPlatformToolOutput {

    /**
     * get JSON data for input
     * @return return JSON
     */
    String getOutputs();

    /**
     * if with error
     * @return err message
     */
    String getError();


    default boolean isSuccess() {
        return StringUtils.isEmpty(getError());
    }

}
