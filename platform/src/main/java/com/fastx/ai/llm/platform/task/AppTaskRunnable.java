package com.fastx.ai.llm.platform.task;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.domains.api.IDubboTaskService;
import com.fastx.ai.llm.domains.api.IDubboWorkflowService;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.dto.TaskDTO;
import com.fastx.ai.llm.domains.dto.TaskExecDTO;
import com.fastx.ai.llm.domains.dto.TaskNodeExecDTO;
import com.fastx.ai.llm.domains.dto.WorkflowVersionDTO;
import com.fastx.ai.llm.platform.context.AppContext;
import com.fastx.ai.llm.platform.tool.exception.ToolExecException;
import com.fastx.ai.llm.platform.tool.nodes.Edge;
import com.fastx.ai.llm.platform.tool.nodes.Node;
import com.fastx.ai.llm.platform.tool.nodes.WorkflowGraph;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author stark
 */
@Slf4j
@Getter
@Setter
public class AppTaskRunnable implements Runnable {

    private TaskDTO task;
    private TaskExecDTO taskExec;

    @Override
    public void run() {
        if (ObjectUtils.anyNull(task, taskExec)) {
            return ;
        }
        try {
            // run this task in thread; clean up before run this task.
            IDubboTaskService taskService = AppContext.getBean(IDubboTaskService.class);
            Assert.isTrue(taskService.deleteTaskNodeExecs(taskExec.getId()), "delete task node execs failed");
            // get workflow version data for task nodes.
            IDubboWorkflowService workflowService = AppContext.getBean(IDubboWorkflowService.class);
            WorkflowVersionDTO workflowVersion =
                    workflowService.getWorkflowVersion(task.getWorkflowVersionId());
            Assert.notNull(workflowVersion, "workflow version is null");
            // create node execute object.
            List<TaskNodeExecDTO> taskNodeExecs =
                    taskService.createTaskNodeExecs(processGraph(workflowVersion.getVersionData()));
            // execute taskNode one by one in another thread.
            taskExec.setStatus(IConstant.RUNNING);
            if (taskService.updateTaskExec(taskExec)) {
                // task run success.
            } else {
                // revoke task.
                Assert.isTrue(taskService.deleteTaskNodeExecs(taskExec.getId()),
                        "revoke task node execs failed! you need check this!");
            }
        } catch (Exception e) {
            log.error("execute task meet exception", e);
        }
    }

    private List<TaskNodeExecDTO> processGraph(String versionData) {
        WorkflowGraph workflowGraph = WorkflowGraph.of(versionData);
        List<Edge> edges = workflowGraph.getEdges();
        if (CollectionUtils.isEmpty(edges)) {
            throw new ToolExecException("edges not found in this version.");
        }
        // create node execute object.
        List<TaskNodeExecDTO> taskNodeExecList = new ArrayList<>();
        for (Edge edge : edges) {
            Optional<TaskNodeExecDTO> node = taskNodeExecList.stream()
                    .filter(t -> StringUtils.equals(t.getNodeId(), edge.getSource())).findFirst();
            TaskNodeExecDTO n = null;
            if (node.isPresent()) {
                n = node.get();
            } else {
                Node gN = workflowGraph.getNode(edge.getSource());
                if (Objects.isNull(gN)) {
                    throw new ToolExecException("node not found for edge in this version.");
                }
                n = new TaskNodeExecDTO();
                n.setTaskExecId(taskExec.getId());
                n.setNodeId(edge.getSource());
                n.setStatus(IConstant.WAIT);
                n.setInputs(JSON.toJSONString(gN.getData().getInnerData().get("inputs")));
                n.setConfig(JSON.toJSONString(Map.of(
                        "tool", gN.getData().getTool(),
                        "organizationId", String.valueOf(task.getOrganizationId())
                )));
            }
            // add target to next source ids.
            String nextNodeIds = StringUtils.defaultIfBlank(n.getNextNodeIds(), "");
            String[] split = nextNodeIds.split(",");
            Set<String> nextNodeIdSet = new HashSet<>(Arrays.asList(split));
            nextNodeIdSet.add(edge.getTarget());
            n.setNextNodeIds(StringUtils.join(nextNodeIdSet, ","));
            // add to taskNode List.
            taskNodeExecList.add(n);
        }
        return taskNodeExecList;
    }
}
