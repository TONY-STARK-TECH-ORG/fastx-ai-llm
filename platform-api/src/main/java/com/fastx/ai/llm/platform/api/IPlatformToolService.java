package com.fastx.ai.llm.platform.api;

import com.fastx.ai.llm.platform.dto.ToolDTO;

import java.util.List;
import java.util.Map;

/**
 * @author stark
 */
public interface IPlatformToolService {

    /**
     * get all platform tools
     * @return tool list
     */
    List<ToolDTO> getPlatformTools();

    /**
     * execute tool with code, version and input data
     * @param toolCode code
     * @param toolVersion version
     * @param type tool type
     * @param input input data
     * @return exec result
     */
    Map<String, Object> execTool(String toolCode, String toolVersion, String type, Map<String, Object> input);
}
