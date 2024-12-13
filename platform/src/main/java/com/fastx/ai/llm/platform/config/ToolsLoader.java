package com.fastx.ai.llm.platform.config;

import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolInput;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;
import jakarta.annotation.PostConstruct;
import org.apache.dubbo.common.utils.Assert;
import org.apache.dubbo.common.utils.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author stark
 */
@Component
public class ToolsLoader {

    static Logger log = LoggerFactory.getLogger(ToolsLoader.class);

    public static Map<String, IPlatformTool<IPlatformToolInput, IPlatformToolOutput>> TOOLS
            = new ConcurrentHashMap<>();
    public static Map<String, Map<String, Map<String, IPlatformTool<IPlatformToolInput, IPlatformToolOutput>>>> TYPE_CODE_VERSION_TOOLS
            = new ConcurrentHashMap<>();
    public static ConcurrentHashSet<IPlatformTool<IPlatformToolInput, IPlatformToolOutput>> TOOL_LIST = new ConcurrentHashSet<>();

    @PostConstruct
    public void init() {
        ServiceLoader<IPlatformTool> serviceLoader = ServiceLoader.load(IPlatformTool.class);
        List<ServiceLoader.Provider<IPlatformTool>> providers = serviceLoader.stream().toList();
        for (ServiceLoader.Provider<IPlatformTool> iPlatformToolProvider
                : providers) {

            IPlatformTool<IPlatformToolInput, IPlatformToolOutput> iPlatformTool = iPlatformToolProvider.get();
            log.info("load tool: {} {} {}", iPlatformTool.getCode(), iPlatformTool.getVersion(), iPlatformTool.getVersion());
            add(iPlatformTool);
        }
    }

    public void addThirdPartTool(IPlatformTool<IPlatformToolInput, IPlatformToolOutput> iPlatformTool) {
        Assert.notNull(iPlatformTool, "tool must not be null");
        add(iPlatformTool);
    }

    private void add(IPlatformTool<IPlatformToolInput, IPlatformToolOutput> iPlatformTool) {
        String code = iPlatformTool.getCode();
        String type = iPlatformTool.getType();
        String version = iPlatformTool.getVersion();
        String status = iPlatformTool.getStatus();
        // add to cached map
        TOOLS.put(code + "|" + version + "|" + type, iPlatformTool);
        // add to level map
        TYPE_CODE_VERSION_TOOLS.putIfAbsent(type, new HashMap<>());
        Map<String, Map<String, IPlatformTool<IPlatformToolInput, IPlatformToolOutput>>> codeToolVersionMap = TYPE_CODE_VERSION_TOOLS.get(type);
        codeToolVersionMap.putIfAbsent(code, new HashMap<>());
        Map<String, IPlatformTool<IPlatformToolInput, IPlatformToolOutput>> toolVersionMap = codeToolVersionMap.get(code);
        toolVersionMap.put(version, iPlatformTool);
        // add to list
        TOOL_LIST.add(iPlatformTool);
    }

    public static IPlatformTool getTool(String code, String version, String type) {
        String codeIdentifier = code + "|" + version + "|" + type;
        IPlatformTool<IPlatformToolInput, IPlatformToolOutput> tool = ToolsLoader.TOOLS.get(codeIdentifier);
        org.springframework.util.Assert.notNull(tool, "tool not found!");
        return tool;
    }

}
