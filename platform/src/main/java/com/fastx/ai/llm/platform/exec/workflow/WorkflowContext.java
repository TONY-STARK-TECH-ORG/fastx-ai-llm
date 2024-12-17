package com.fastx.ai.llm.platform.exec.workflow;

import com.fastx.ai.llm.platform.tool.nodes.WorkflowGraph;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author stark
 */
@Data
public class WorkflowContext {
    private Map<String, Object> inputs = new ConcurrentHashMap<>();
    private Map<String, Object> modifiedInputs = new ConcurrentHashMap<>();
    private Map<String, Object> outputs = new ConcurrentHashMap<>();
    private Map<String, Object> config = new ConcurrentHashMap<>();
    private Map<String, String> execLogs = new ConcurrentHashMap<>();

    public Map<String, Map<String, Object>> getExpressionContext() {
        Map<String, Map<String, Object>> execContext = new HashMap<>();
        inputs.forEach((k, v) -> {
            execContext.putIfAbsent(k, new HashMap<>());
            execContext.get(k).put("inputs", v);
        });
        outputs.forEach((k, v) -> {
            execContext.putIfAbsent(k, new HashMap<>());
            execContext.get(k).put("outputs", v);
        });
        return execContext;
    }

    private WorkflowGraph graph;
}
