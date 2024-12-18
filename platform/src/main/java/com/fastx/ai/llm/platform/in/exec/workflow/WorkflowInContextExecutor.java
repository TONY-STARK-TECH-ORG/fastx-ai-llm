package com.fastx.ai.llm.platform.in.exec.workflow;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.domains.api.IDubboToolService;
import com.fastx.ai.llm.platform.config.ToolsLoader;
import com.fastx.ai.llm.platform.tool.llm.LLMInput;
import com.fastx.ai.llm.platform.tool.nodes.Edge;
import com.fastx.ai.llm.platform.tool.nodes.Node;
import com.fastx.ai.llm.platform.tool.nodes.WorkflowGraph;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolInput;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;
import com.fastx.ai.llm.platform.tool.train.TrainInput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author stark
 */
@Slf4j
@Component
public class WorkflowInContextExecutor {

    @Autowired
    ToolsLoader toolsLoader;
    @Autowired
    IDubboToolService toolService;

    public void execute() {
        // get context !
        WorkflowInContext context = WorkflowInContextHolder.getContext();
        WorkflowGraph graph = context.getGraph();
        // execute nodes.
        executeNode(graph.findStartNode(), graph);
    }

    public void executeNode(Node node, WorkflowGraph graph) {
        String type = node.getData().getTool().getType();
        IPlatformTool tool = ToolsLoader.getTool(
                node.getData().getTool().getCode(),
                node.getData().getTool().getVersion(),
                type
        );
        // execute current
        executeNode(tool, node, graph);
        // get next Nodes
        Node next = graph.getNode(graph.getNextNodeId(node.getId()));
        if (null == next || next.isExecuted()) {
            return ;
        } else {
            executeNode(next, graph);
        }
    }

    public void executeNode(IPlatformTool tool, Node node, WorkflowGraph graph) {
        WorkflowInContext context = WorkflowInContextHolder.getContext();
        IPlatformToolInput in;
        if (StringUtils.startsWith(tool.getType(), "llm-")) {
            in = new LLMInput();
        } else {
            in = new TrainInput();
        }
        Map<String, Object> innerData = node.getData().getInnerData();
        String nodeName= node.getData().getName();

        // set config to tool input.
        in.setConfig(JSON.toJSONString(context.getConfig().get(nodeName)));

        // get all inner data.
        List<Edge> innerEdges = graph.findInnerEdges(node);

        if (innerEdges.size() != 0) {
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext c = new StandardEvaluationContext();
            c.addPropertyAccessor(new MapAccessor());
            c.setVariable("context", context.getExpressionContext());

            // have prev node. we should exec inputs now.
            Map<String, Object> inputs = (Map) innerData.get("inputs");
            Map<String, Object> executeInputs = new HashMap<>();
            for (String key : inputs.keySet()) {
                Object value = inputs.get(key);
                if (value instanceof String) {
                    try {
                        executeInputs.put(key, parser.parseExpression("#context." + (String) value).getValue(c));
                    } catch (Exception e) {
                        log.error("get expression data error!", e);
                    }
                } else if (value instanceof List) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) value;
                    List<Map<String, Object>> executeList = new ArrayList<>();
                    list.forEach(item -> {
                        Map<String, Object> executeItem = new HashMap<>();
                        item.forEach((k, v) -> {
                            if (v instanceof String) {
                                try {
                                    executeItem.put(k, parser.parseExpression("#context." + (String) v).getValue(c));
                                } catch (Exception e) {
                                    log.error("get expression data error!", e);
                                }
                            } else {
                                executeItem.put(k, v);
                            }
                        });
                        executeList.add(executeItem);
                    });
                    executeInputs.put(key, executeList);
                } else {
                    executeInputs.put(key, value);
                }
            }
            context.getModifiedInputs().put(nodeName, executeInputs);
            in.setData(JSON.toJSONString(executeInputs));
        } else {
            in.setData(JSON.toJSONString(innerData.get("inputs")));
        }
        IPlatformToolOutput output = tool.exec(in);
        // set to context, for next use.
        innerData.put("outputs", JSON.parseObject(output.getData(), Map.class));
        context.getOutputs().put(nodeName, output.isSuccess() ? JSON.parseObject(output.getData()) : output.getError());
        // set executed!
        node.setExecuted(true);
    }
}
