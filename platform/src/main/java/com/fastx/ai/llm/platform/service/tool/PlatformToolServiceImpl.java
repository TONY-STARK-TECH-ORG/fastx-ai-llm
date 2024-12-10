package com.fastx.ai.llm.platform.service.tool;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.platform.api.IPlatformToolService;
import com.fastx.ai.llm.platform.config.ToolsLoader;
import com.fastx.ai.llm.platform.dto.ToolDTO;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolInput;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;
import org.apache.dubbo.common.utils.ConcurrentHashSet;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author stark
 */
@DubboService
public class PlatformToolServiceImpl implements IPlatformToolService {

    @Autowired
    ToolsLoader toolsLoader;

    @Override
    @SentinelResource("platform.tool.get")
    public List<ToolDTO> getPlatformTools() {
        ConcurrentHashSet<IPlatformTool<IPlatformToolInput, IPlatformToolOutput>> toolList = ToolsLoader.TOOL_LIST;
        return toolList.stream().map(tool -> {
            ToolDTO toolDTO = new ToolDTO();
            toolDTO.setCode(tool.getCode());
            toolDTO.setType(tool.getType());
            toolDTO.setAuthor(tool.getAuthor());
            toolDTO.setVersion(tool.getVersion());
            toolDTO.setPrototype(tool.getPrototype());
            toolDTO.setStatus(tool.getStatus());
            return toolDTO;
        }).collect(Collectors.toList());
    }
}
