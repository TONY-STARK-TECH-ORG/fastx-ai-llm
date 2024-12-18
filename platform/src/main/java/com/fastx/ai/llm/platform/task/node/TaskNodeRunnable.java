package com.fastx.ai.llm.platform.task.node;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.domains.api.IDubboTaskService;
import com.fastx.ai.llm.domains.api.IDubboToolService;
import com.fastx.ai.llm.domains.api.IDubboWorkflowService;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.dto.*;
import com.fastx.ai.llm.platform.config.ToolsLoader;
import com.fastx.ai.llm.platform.context.AppContext;
import com.fastx.ai.llm.platform.exception.TaskNodeExecException;
import com.fastx.ai.llm.platform.tool.nodes.NodeTool;
import com.fastx.ai.llm.platform.tool.nodes.WorkflowGraph;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolInput;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;
import com.fastx.ai.llm.platform.tool.spi.SimplePlatformToolInput;
import com.rometools.utils.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author stark
 */
@Slf4j
@Getter
@Setter
public class TaskNodeRunnable implements Runnable {

    private TaskNodeExecDTO taskNodeExec;
    private TaskExecDTO taskExec;
    private TaskDTO task;

    @Override
    public void run() {
        if (Objects.isNull(taskNodeExec)) {
            return ;
        }
        try {
            String startTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
            // find organization configuration for this tool.
            // @TODO (stark) system tools which not need config should be auto configured.
            IDubboToolService toolService = AppContext.getBean(IDubboToolService.class);
            OrganizationToolsDTO orgTool =
                    toolService.getOrganizationToolById(Long.parseLong(getOrganizationToolId()));
            Assert.notNull(orgTool, "tool not found");
            // tool execute.
            IDubboTaskService taskService = AppContext.getBean(IDubboTaskService.class);
            List<TaskNodeExecDTO> parentNodes =
                    Lists.createWhenNull(
                            taskService.getParentChainTaskNodeExecByNodeId(taskNodeExec.getNodeId()));
            IDubboWorkflowService workflowService = AppContext.getBean(IDubboWorkflowService.class);
            WorkflowVersionDTO workflowVersion =
                    workflowService.getWorkflowVersion(task.getWorkflowVersionId());
            if (Objects.isNull(workflowVersion)) {
                throw new TaskNodeExecException("workflow version not found");
            }
            WorkflowGraph graph = WorkflowGraph.of(workflowVersion.getVersionData());

            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.addPropertyAccessor(new MapAccessor());

            for (TaskNodeExecDTO parentNode : parentNodes) {
                context.setVariable(getNodeName(parentNode), JSON.parseObject(parentNode.getOutputs(), Map.class));
            }
            // prepare spel context
            Map<String, Object> executableInputs =
                    getExecutableInputs(parser, context, JSON.parseObject(taskNodeExec.getInputs(), Map.class), parentNodes);
            NodeTool currentTool = getTool();
            IPlatformTool tool =
                    ToolsLoader.getTool(currentTool.getCode(), currentTool.getVersion(), currentTool.getType());
            // build input.
            IPlatformToolInput in = new SimplePlatformToolInput();
            in.setInputs(JSON.toJSONString(executableInputs));
            // remove stream config.
            Map<String, Object> _config = JSON.parseObject(orgTool.getConfigData(), Map.class);
            _config.remove("streaming");
            in.setConfig(JSON.toJSONString(_config));
            // exec this tool.
            IPlatformToolOutput exec = tool.exec(in);
            if (exec.isSuccess()) {
                taskNodeExec.setOutputs(exec.getOutputs());
            } else {
                taskNodeExec.setOutputs(JSON.toJSONString(Map.of("error", exec.getError())));
            }
            String endTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
            // set exec finish.
            taskNodeExec.setStatus(IConstant.FINISH);
            taskNodeExec.setStartTime(startTime);
            taskNodeExec.setEndTime(endTime);
            Assert.isTrue(taskService.updateTaskNodeExecs(taskNodeExec),
                    "exec finished, update task node exec state failed!");
            // check all not execute.
            if (taskService.isTaskExecNodeFinished(taskExec.getId())) {
                taskExec.setStatus(IConstant.FINISH);
                taskExec.setCompleteTime(LocalDateTime.now());
                Assert.isTrue(taskService.updateTaskExec(taskExec),
                        "exec finished, update task exec state failed!");
            }
        } catch (Exception e) {
            log.error("execute tool node meet exception", e);
        }
    }

    private Map<String, Object> getExecutableInputs(
            ExpressionParser parser, StandardEvaluationContext _context, Map<String, Object> _inputs, List<TaskNodeExecDTO> _nodes) {
        Map<String, Object> inputs = new HashMap<>();
        for (Map.Entry<String, Object> entry : _inputs.entrySet()) {
            Object _value = entry.getValue();
            if (_value instanceof String) {
                // parse string type
                String v = (String) _value;
                Optional<TaskNodeExecDTO> _find = _nodes.stream()
                        .filter(n -> v.startsWith(getNodeName(n) + ".")).findFirst();
                if (_find.isPresent()) {
                    try {
                        Expression exp = parser.parseExpression("#" + v);
                        inputs.put(entry.getKey(), exp.getValue(_context));
                    } catch (Exception e) {
                        inputs.put(entry.getKey(), _value);
                    }
                } else {
                    inputs.put(entry.getKey(), _value);
                }
            } else if (_value instanceof Collection) {
                // parse list type
                List<Object> v = (List<Object>) _value;
                if (CollectionUtils.isEmpty(v)) {
                    continue ;
                }
                if (v.get(0) instanceof String) {
                    List<String> _iv = (List<String>) _value;
                    List<Object> executableList = new ArrayList<>();
                    _iv.forEach(_iiv -> {
                        Optional<TaskNodeExecDTO> _find = _nodes.stream()
                                .filter(n -> _iiv.startsWith(getNodeName(n) + ".")).findFirst();
                        if (_find.isPresent()) {
                            try {
                                Expression exp = parser.parseExpression("#" + v);
                                executableList.add(exp.getValue(_context));
                            } catch (Exception e) {
                                executableList.add(_iiv);
                            }
                        } else {
                            executableList.add(_iiv);
                        }
                    });
                    inputs.put(entry.getKey(), executableList);
                } else if (v.get(0) instanceof Map) {
                    List<Map<String, Object>> _iv = (List<Map<String, Object>>) _value;
                    List<Map<String, Object>> executableList = new ArrayList<>();
                    _iv.forEach(_iiv -> {
                        Map<String, Object> _executableInnerMap = new HashMap<>();
                        for (Map.Entry<String, Object> _iive : _iiv.entrySet()) {
                            if (_iive.getValue() instanceof String) {
                                String _iivee = (String) _iive.getValue();
                                Optional<TaskNodeExecDTO> _find = _nodes.stream()
                                        .filter(n -> _iivee.startsWith(getNodeName(n) + ".")).findFirst();
                                if (_find.isPresent()) {
                                    try {
                                        Expression exp = parser.parseExpression("#" + _iivee);
                                        _executableInnerMap.put(_iive.getKey(), exp.getValue(_context));
                                    } catch (Exception e) {
                                        _executableInnerMap.put(_iive.getKey(), _iive.getValue());
                                    }
                                } else {
                                    _executableInnerMap.put(_iive.getKey(), _iive.getValue());
                                }
                            }
                        }
                        executableList.add(_executableInnerMap);
                    });
                    inputs.put(entry.getKey(), executableList);
                } else {
                    inputs.put(entry.getKey(), _value);
                }
            } else if (_value instanceof Map) {
                // parse map type
                Map<String, Object> _iv = (Map<String, Object>) _value;
                Map<String, Object> executableInnerMap = new HashMap<>();
                for (Map.Entry<String, Object> _ive : _iv.entrySet()) {
                    if (_ive.getValue() instanceof String) {
                        String _ivee = (String) _ive.getValue();
                        Optional<TaskNodeExecDTO> _find = _nodes.stream()
                                .filter(n -> _ivee.startsWith(getNodeName(n) + ".")).findFirst();
                        if (_find.isPresent()) {
                            try {
                                Expression exp = parser.parseExpression("#" + _ivee);
                                executableInnerMap.put(_ive.getKey(), exp.getValue(_context));
                            } catch (Exception e) {
                                executableInnerMap.put(_ive.getKey(), _ive.getValue());
                            }
                        } else {
                            executableInnerMap.put(_ive.getKey(), _ive.getValue());
                        }
                    }
                }
                inputs.put(entry.getKey(), executableInnerMap);
            } else {
                inputs.put(entry.getKey(), _value);
            }
        }
        return inputs;
    }

    private NodeTool getTool() {
        String config = taskNodeExec.getConfig();
        if (StringUtils.isEmpty(config)) {
            throw new TaskNodeExecException("task node config is empty");
        }
        NodeTool nodeTool = JSON.parseObject(config).toJavaObject(NodeTool.class);
        if (Objects.isNull(nodeTool)) {
            throw new TaskNodeExecException("task node config is invalid (without tool info)");
        }
        return nodeTool;
    }

    private String getOrganizationToolId() {
        String config = taskNodeExec.getConfig();
        if (StringUtils.isEmpty(config)) {
            throw new TaskNodeExecException("task node config is empty");
        }
        String organizationToolId = JSON.parseObject(config).getString("organizationToolId");
        if (StringUtils.isEmpty(organizationToolId)) {
            throw new TaskNodeExecException("task node config is invalid (without organizationToolId)");
        }
        return organizationToolId;
    }

    private String getNodeName(TaskNodeExecDTO nodeExec) {
        String config = nodeExec.getConfig();
        if (StringUtils.isEmpty(config)) {
            throw new TaskNodeExecException("task node config is empty");
        }
        String nodeName = JSON.parseObject(config).getString("nodeName");
        if (StringUtils.isEmpty(nodeName)) {
            throw new TaskNodeExecException("task node config is invalid (without nodeName)");
        }
        return nodeName;
    }
}
