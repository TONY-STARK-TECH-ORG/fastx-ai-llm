package com.fastx.ai.llm.platform.config;

import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolInput;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author stark
 */
@Configuration
public class ToolsLoader {

    static Map<String, IPlatformTool<IPlatformToolInput, IPlatformToolOutput>> TOOLS
            = new ConcurrentHashMap<>();
    static Map<String, Map<String, Map<String, IPlatformTool<IPlatformToolInput, IPlatformToolOutput>>>> TYPE_CODE_VERSION_TOOLS
            = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        ServiceLoader<IPlatformTool> serviceLoader = ServiceLoader.load(IPlatformTool.class);
        for (ServiceLoader.Provider<IPlatformTool> iPlatformToolProvider
                : serviceLoader.stream().toList()) {

            IPlatformTool<IPlatformToolInput, IPlatformToolOutput> iPlatformTool = iPlatformToolProvider.get();
            String code = iPlatformTool.getCode();
            String type = iPlatformTool.getType();
            String version = iPlatformTool.getVersion();
            String status = iPlatformTool.getStatus();
            if (!IConstant.ACTIVE.equals(status)) {
                continue ;
            }
            // add to cached map
            TOOLS.put(code + "|" + version + "|" + type, iPlatformTool);
            // add to level map
            TYPE_CODE_VERSION_TOOLS.putIfAbsent(type, new HashMap<>());
            Map<String, Map<String, IPlatformTool<IPlatformToolInput, IPlatformToolOutput>>> codeToolVersionMap = TYPE_CODE_VERSION_TOOLS.get(code);
            codeToolVersionMap.putIfAbsent(code, new HashMap<>());
            Map<String, IPlatformTool<IPlatformToolInput, IPlatformToolOutput>> toolVersionMap = codeToolVersionMap.get(code);
            toolVersionMap.put(version, iPlatformTool);
        }
    }

}
