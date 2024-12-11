package com.fastx.ai.llm.platform.service.tool;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.platform.api.IPlatformToolService;
import com.fastx.ai.llm.platform.config.ToolsLoader;
import com.fastx.ai.llm.platform.dto.ToolDTO;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolInput;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;
import com.fastx.ai.llm.platform.tool.train.TrainInput;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.common.utils.ConcurrentHashSet;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    @SentinelResource("platform.tool.exec")
    public Map<String, Object> execTool(String toolCode, String toolVersion, String type, Map<String, Object> input) {
        // get tool by code and version
        IPlatformTool<IPlatformToolInput, IPlatformToolOutput> tool = getTool(toolCode, toolVersion, type);
        IPlatformToolInput in;
        if ("llm-model".equals(type)) {
            in = new LLMInput();
        } else {
            in = new TrainInput();
        }
        if (input.containsKey("config")) {
            in.setConfig(JSON.toJSONString(input.get("config")));
        }
        if (input.containsKey("data")) {
            in.setData(JSON.toJSONString(input.get("data")));
        }
        if (input.containsKey("stream")) {
            // TODO (stark): support stream, use dubbo stream, and new thread to exec tool.
            in.setStream(null);
        }
        IPlatformToolOutput exec = tool.exec(in);
        return Map.of("data", ObjectUtils.defaultIfNull(exec.getData(), new HashMap<>()), "error", ObjectUtils.defaultIfNull(exec.getError(), ""));
    }

    private IPlatformTool getTool(String code, String version, String type) {
        String codeIdentifier = code + "|" + version + "|" + type;
        IPlatformTool<IPlatformToolInput, IPlatformToolOutput> tool = ToolsLoader.TOOLS.get(codeIdentifier);
        Assert.notNull(tool, "tool not found!");
        return tool;
    }
}
