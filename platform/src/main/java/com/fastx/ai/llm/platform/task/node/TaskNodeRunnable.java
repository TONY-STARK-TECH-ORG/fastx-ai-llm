package com.fastx.ai.llm.platform.task.node;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.domains.api.IDubboTaskService;
import com.fastx.ai.llm.domains.api.IDubboToolService;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.dto.OrganizationToolsDTO;
import com.fastx.ai.llm.domains.dto.TaskDTO;
import com.fastx.ai.llm.domains.dto.TaskExecDTO;
import com.fastx.ai.llm.domains.dto.TaskNodeExecDTO;
import com.fastx.ai.llm.platform.config.ToolsLoader;
import com.fastx.ai.llm.platform.context.AppContext;
import com.fastx.ai.llm.platform.exception.TaskNodeExecException;
import com.fastx.ai.llm.platform.tool.nodes.NodeTool;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolInput;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;
import com.fastx.ai.llm.platform.tool.spi.SimplePlatformToolInput;
import com.fastx.ai.llm.platform.utils.AppExpressionParser;
import com.rometools.utils.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private String orgToolConfig;

    @Override
    public void run() {
        if (Objects.isNull(taskNodeExec)) {
            return ;
        }
        try {
            NodeTool currentTool = getTool();
            IPlatformTool tool =
                    ToolsLoader.getTool(currentTool.getCode(), currentTool.getVersion(), currentTool.getType());;
            String startTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
            // find organization configuration for this tool.
            if (tool.needConfig()) {
                IDubboToolService toolService = AppContext.getBean(IDubboToolService.class);
                OrganizationToolsDTO orgTool =
                        toolService.getOrganizationToolById(Long.parseLong(getOrganizationToolId()));
                Assert.notNull(orgTool, "org tool config not found");
                orgToolConfig = orgTool.getConfigData();
            }
            // tool execute.
            IDubboTaskService taskService = AppContext.getBean(IDubboTaskService.class);
            List<TaskNodeExecDTO> parentNodes =
                    Lists.createWhenNull(
                            taskService.getParentChainTaskNodeExecByNodeId(taskNodeExec.getNodeId()));
            // build exec used context.
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.addPropertyAccessor(new MapAccessor());
            for (TaskNodeExecDTO parentNode : parentNodes) {
                context.setVariable(getNodeName(parentNode), JSON.parseObject(parentNode.getOutputs(), Map.class));
            }
            // prepare spel context
            Map<String, Object> executableInputs = AppExpressionParser.getInputs(
                    parser, context, taskNodeExec.getInputs());
            // build input.
            IPlatformToolInput in = new SimplePlatformToolInput();
            in.setInputs(JSON.toJSONString(executableInputs));
            if (tool.needConfig()) {
                // remove stream config.
                Map<String, Object> _config = JSON.parseObject(orgToolConfig, Map.class);
                _config.remove("streaming");
                in.setConfig(JSON.toJSONString(_config));
            }
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
