package com.fastx.ai.llm.platform.task;

import com.fastx.ai.llm.domains.api.IDubboTaskService;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.dto.PageDTO;
import com.fastx.ai.llm.domains.dto.TaskDTO;
import com.fastx.ai.llm.domains.dto.TaskExecDTO;
import com.fastx.ai.llm.domains.dto.TaskNodeExecDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author stark
 */
@Slf4j
@Component
public class AppTaskRunner {

    @DubboReference
    IDubboTaskService taskService;

    @Autowired
    AppTaskExecutor appTaskExecutor;

    /**
     * this task used for knowledge base task, like file embedding, image embedding, etc.
     * which will be executed by a fixed rate scheduler.
     */
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void scheduleFixedRunnerForKnowledgeBaseTask() {
        log.info("scheduleFixedRunnerForKnowledgeBaseTask::started");
        startTaskExec(1L, 5L, IConstant.WAIT, IConstant.KNOWLEDGE_BASE);
        log.info("scheduleFixedRunnerForKnowledgeBaseTask::finished full search.");
    }

    public void startTaskExec(Long page, Long size, String status, String type) {
        PageDTO<TaskExecDTO> taskExecs =
                taskService.getTaskExecs(page, size, status, type);

        if (0 == taskExecs.getSize()) {
            return ;
        }

        for (TaskExecDTO t : taskExecs.getList()) {
            log.info("scheduleFixedRunnerForKnowledgeBaseTask::taskExec: task exec: {}, task: {}",
                    t.getId(), t.getTaskId());
            try {
                // before execute, check local execute pool state.
                if (!appTaskExecutor.canSubmit()) {
                    log.warn("scheduleFixedRunnerForKnowledgeBaseTask::task exec: {}, task: {} " +
                            "submit failed, local thread pool is full.", t.getId(), t.getTaskId());
                    // stop this time task exec.
                    return ;
                }
                // query task
                TaskDTO task = taskService.getTaskById(t.getTaskId());
                // check task status
                if (!IConstant.ACTIVE.equals(task.getStatus())) {
                    continue;
                }
                // query task nodes
                List<TaskNodeExecDTO> taskNodeExecs =
                        taskService.getTaskNodeExecs(t.getId());
                if (CollectionUtils.isEmpty(taskNodeExecs)) {
                    continue;
                }
                // Modified this state to.
                t.setStatus(IConstant.RUNNING);
                // execute task now.
                if (!taskService.updateTaskExec(t)) {
                    continue ;
                }
                // submit to thread pool to execute.
                AppTaskRunnable context = new AppTaskRunnable();
                context.setTask(task);
                context.setTaskExec(t);
                context.setTaskNodeExecList(taskNodeExecs);
                // to thread pool execute.
                appTaskExecutor.execute(context);
            } catch (Exception e) {
                if (StringUtils.contains(e.getMessage().toLowerCase(), "lock")) {
                    // ignored.
                } else {
                    log.error("scheduleFixedRunnerForKnowledgeBaseTask::updateTaskExec failed", e);
                }
            }
        }
    }

}
