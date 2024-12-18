package com.fastx.ai.llm.platform.task.node;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.domains.api.IDubboTaskService;
import com.fastx.ai.llm.domains.api.IDubboToolService;
import com.fastx.ai.llm.domains.dto.OrganizationToolsDTO;
import com.fastx.ai.llm.domains.dto.TaskNodeExecDTO;
import com.fastx.ai.llm.platform.context.AppContext;
import com.fastx.ai.llm.platform.exception.TaskNodeExecException;
import com.fastx.ai.llm.platform.tool.nodes.NodeTool;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author stark
 */
@Slf4j
@Getter
@Setter
public class TaskNodeRunnable implements Runnable {

    private TaskNodeExecDTO taskNodeExec;

    @Override
    public void run() {
        if (Objects.isNull(taskNodeExec)) {
            return ;
        }
        try {
            // find organization configuration for this tool.
            // @TODO (stark) system tools which not need config should be auto configured.
            IDubboToolService toolService = AppContext.getBean(IDubboToolService.class);
            OrganizationToolsDTO orgTool =
                    toolService.getOrganizationToolById(Long.parseLong(getOrganizationId()));
            Assert.notNull(orgTool, "tool not found");
            // tool execute.
            IDubboTaskService taskService = AppContext.getBean(IDubboTaskService.class);
            // @TODO (stark) 1. query prev nodes (check all finished, get outputs.)
            // @TODO (stark) 2. build context to execute current node.
            // @TODO (stark) 3. save node execute result to db.
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
            throw new TaskNodeExecException("task node config is invalid");
        }
        return nodeTool;
    }

    private String getOrganizationId() {
        String config = taskNodeExec.getConfig();
        if (StringUtils.isEmpty(config)) {
            throw new TaskNodeExecException("task node config is empty");
        }
        String organizationId = JSON.parseObject(config).getString("organizationId");
        if (StringUtils.isEmpty(organizationId)) {
            throw new TaskNodeExecException("task node config is invalid");
        }
        return organizationId;
    }
}
