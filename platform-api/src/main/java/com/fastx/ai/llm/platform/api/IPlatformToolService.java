package com.fastx.ai.llm.platform.api;

import com.fastx.ai.llm.platform.dto.ToolDTO;
import org.apache.dubbo.common.stream.StreamObserver;

import java.io.IOException;
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

    /**
     * execute tool with code, version and input data
     * @param request json (dubbo not support other fields.)
     * @param response resp
     * @throws IOException pipe io exception
     */
    void streamExecTool(String request, StreamObserver<String> response) throws IOException;
}
