package com.fastx.ai.llm.platform.task.node;

import com.fastx.ai.llm.domains.api.IDubboTaskService;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.dto.PageDTO;
import com.fastx.ai.llm.domains.dto.TaskExecDTO;
import com.fastx.ai.llm.domains.dto.TaskNodeExecDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author stark
 */
@Slf4j
@Component
public class TaskNodeRunner {

    @DubboReference
    IDubboTaskService taskService;

    @Autowired
    TaskNodeExecutor taskNodeExecutor;

    /**
     * this task used for knowledge base task, like file embedding, image embedding, etc.
     * which will be executed by a fixed rate scheduler.
     */
    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.MINUTES)
    public void scheduleFixedRunnerForTaskNodes() {
        log.info("scheduleFixedRunnerForTaskNodes::started");
        startTaskNodeExec(1L, 5L, IConstant.WAIT);
        log.info("scheduleFixedRunnerForTaskNodes::finished full search.");
    }

    public void startTaskNodeExec(Long page, Long size, String status) {
        PageDTO<TaskNodeExecDTO> nodeExecs =
                taskService.getTaskNodeExecs(page, size, status, true);

        if (0 == nodeExecs.getSize()) {
            return ;
        }

        for (TaskNodeExecDTO t : nodeExecs.getList()) {
            log.info("scheduleFixedRunnerForTaskNodes::taskNodeExec: task node : {}, task exec: {}, node id: {}",
                    t.getId(), t.getTaskExecId(), t.getNodeId());
            try {
                // query task exec.
                TaskExecDTO taskExec = taskService.getTaskExecById(t.getTaskExecId());
                // check task exec status
                if (!IConstant.RUNNING.equals(taskExec.getStatus())) {
                    continue;
                }
                // @TODO (stark) check prev nodes all finished. and set to execute context.
                // before execute, check local execute pool state.
                if (!taskNodeExecutor.canSubmit()) {
                    log.warn("scheduleFixedRunnerForTaskNodes:: task node : {}, task exec: {}, node id: {} , submit failed, local thread pool is full.",
                            t.getId(), t.getTaskExecId(), t.getNodeId());
                    // stop this task now.
                    return ;
                }
                // Modified this node state to.
                t.setStatus(IConstant.RUNNING);
                // execute task now.
                if (!taskService.updateTaskNodeExecs(t)) {
                    continue ;
                }
                // submit to thread pool to execute.
                TaskNodeRunnable context = new TaskNodeRunnable();
                context.setTaskNodeExec(t);
                taskNodeExecutor.execute(context);
            } catch (Exception e) {
                if (StringUtils.contains(e.getMessage().toLowerCase(), "lock")) {
                    // ignored.
                    log.info("scheduleFixedRunnerForTaskNodes::updateTaskNodeExec failed", e);
                    continue ;
                }
                log.error("scheduleFixedRunnerForTaskNodes::taskNodeExec meet exception", e);
            }
        }
    }

}
