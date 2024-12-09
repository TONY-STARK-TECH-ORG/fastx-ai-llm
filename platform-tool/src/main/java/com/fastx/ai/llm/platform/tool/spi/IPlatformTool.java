package com.fastx.ai.llm.platform.tool.spi;

/**
 * @author stark
 */
public interface IPlatformTool<Input extends IPlatformToolInput, Output extends IPlatformToolOutput> {

    /**
     * unique code for this tool
     * @return code like 'x.y.z'
     */
    String getCode();

    /**
     * type for this tool
     * @return llm-tool, llm-function ...
     */
    String getType();

    /**
     * author for this tool
     * @return author name
     */
    String getAuthor();

    /**
     * version for this tool
     * @return version str
     */
    String getVersion();

    /**
     * structure for this tool that contains: input fields, output fields and more
     * @return JSON
     */
    String getPrototype();

    /**
     * tool status
     * @return active or inactive
     */
    String getStatus();

    /**
     * exec local need override this method.
     * @param input input model
     * @return output model
     */
    Output exec(Input input);
}
