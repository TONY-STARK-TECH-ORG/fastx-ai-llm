package com.fastx.ai.llm.platform.service.tool;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.platform.api.IPlatformToolService;
import com.fastx.ai.llm.platform.config.ToolsLoader;
import com.fastx.ai.llm.platform.dto.ToolDTO;
import com.fastx.ai.llm.platform.exec.tool.PlatformToolExecutor;
import com.fastx.ai.llm.platform.exec.tool.ToolContext;
import com.fastx.ai.llm.platform.exec.tool.ToolRunner;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolInput;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;
import com.fastx.ai.llm.platform.tool.train.TrainInput;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.common.utils.ConcurrentHashSet;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author stark
 */
@DubboService
public class PlatformToolServiceImpl implements IPlatformToolService {

    private Logger logger = LoggerFactory.getLogger(PlatformToolServiceImpl.class);

    @Autowired
    ToolsLoader toolsLoader;

    @Autowired
    PlatformToolExecutor toolExecutor;

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
            toolDTO.setName(tool.getName());
            toolDTO.setDescription(tool.getDescription());
            toolDTO.setIcon(tool.getIcon());
            return toolDTO;
        }).toList();
    }

    @Override
    @SentinelResource("platform.tool.exec")
    public Map<String, Object> execTool(String toolCode, String toolVersion, String type, Map<String, Object> input) {
        // get tool by code and version
        IPlatformTool<IPlatformToolInput, IPlatformToolOutput> tool = ToolsLoader.getTool(toolCode, toolVersion, type);
        Assert.notNull(tool, "tool not found!");
        IPlatformToolInput in;
        if (StringUtils.startsWith(type, "llm-")) {
            in = new LLMInput();
        } else {
            in = new TrainInput();
        }
        if (input.containsKey("config")) {
            in.setConfig(JSON.toJSONString(input.get("config")));
        }
        if (input.containsKey("inputs")) {
            in.setData(JSON.toJSONString(input.get("inputs")));
        }
        IPlatformToolOutput exec = tool.exec(in);
        return Map.of(
                "data", ObjectUtils.defaultIfNull(exec.getData(), new HashMap<>()),
                "error", ObjectUtils.defaultIfNull(exec.getError(), ""),
                "success", exec.isSuccess());
    }

    @Override
    @SentinelResource("platform.tool.exec")
    public void streamExecTool(String request, StreamObserver<String> response) throws IOException, InterruptedException {
        ToolContext context = ToolContext.of(JSON.parseObject(request, Map.class));
        // set context and execute tool.
        toolExecutor.setContext(context);
        toolExecutor.execute(new ToolRunner());
        // read to stream
        context.readTo(response, logger);
        context.clean();
        // complete stream
        response.onCompleted();
    }
}
