package com.fastx.ai.llm.platform.task;

import com.fastx.ai.llm.domains.api.IDubboTaskService;
import com.fastx.ai.llm.domains.api.IDubboWorkflowService;
import com.fastx.ai.llm.domains.dto.TaskDTO;
import com.fastx.ai.llm.domains.dto.TaskExecDTO;
import com.fastx.ai.llm.domains.dto.TaskNodeExecDTO;
import com.fastx.ai.llm.domains.dto.WorkflowVersionDTO;
import com.fastx.ai.llm.platform.context.AppContext;
import com.fastx.ai.llm.platform.tool.nodes.WorkflowGraph;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author stark
 */
@Slf4j
@Getter
@Setter
public class AppTaskRunnable implements Runnable {

    private TaskDTO task;
    private TaskExecDTO taskExec;
    private List<TaskNodeExecDTO> taskNodeExecList;

    @Override
    public void run() {
        if (ObjectUtils.anyNull(task, taskExec) || CollectionUtils.isEmpty(taskNodeExecList)) {
            return ;
        }
        try {
            // run this task in thread.
            // get workflow version data for task nodes.
            IDubboWorkflowService workflowService = AppContext.getBean(IDubboWorkflowService.class);
            WorkflowVersionDTO workflowVersion =
                    workflowService.getWorkflowVersion(task.getWorkflowVersionId());
            Assert.notNull(workflowVersion, "workflow version is null");
            // get node data in this workflow version.
            String versionData = workflowVersion.getVersionData();
            WorkflowGraph workflowGraph = WorkflowGraph.of(versionData);
            // create node execute object.
            IDubboTaskService taskService = AppContext.getBean(IDubboTaskService.class);
            // @TODO (stark) taskService.createTaskNodeExecs()
            // update task state.
        } catch (Exception e) {
            log.error("execute task meet exception", e);
        }
    }
}
