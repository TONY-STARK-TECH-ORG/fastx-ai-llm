package com.fastx.ai.llm.platform.api;

import com.fastx.ai.llm.platform.dto.ToolDTO;

import java.util.List;

/**
 * @author stark
 */
public interface IPlatformToolService {

    /**
     * get all platform tools
     * @return tool list
     */
    List<ToolDTO> getPlatformTools();
}
