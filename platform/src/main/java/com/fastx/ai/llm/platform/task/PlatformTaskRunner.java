package com.fastx.ai.llm.platform.task;

import com.fastx.ai.llm.domains.api.IDubboTaskService;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.dto.PageDTO;
import com.fastx.ai.llm.domains.dto.TaskExecDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author stark
 */
@Slf4j
@Component
public class PlatformTaskRunner {

    @DubboReference
    IDubboTaskService taskService;

    /**
     * this task used for knowledge base task, like file embedding, image embedding, etc.
     * which will be executed by a fixed rate scheduler.
     */
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void scheduleFixedRunnerForKnowledgeBaseTask() {
        log.error("scheduleFixedRunnerForKnowledgeBaseTask::started");
        PageDTO<TaskExecDTO> taskExecs =
                taskService.getTaskExecs(1L, 5L, IConstant.WAIT, IConstant.KNOWLEDGE_BASE);
        taskExecs.getList().forEach(taskExec -> {
            log.error("scheduleFixedRunnerForKnowledgeBaseTask::taskExec:{}", taskExec);
            // Modified this state to.
        });
        log.error("scheduleFixedRunnerForKnowledgeBaseTask::started");
    }

}
