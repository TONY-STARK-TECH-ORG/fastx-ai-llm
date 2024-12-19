package com.fastx.ai.llm.platform.task.knowledge;

import com.fastx.ai.llm.domains.api.IDubboKnowledgeBaseService;
import com.fastx.ai.llm.domains.constant.IConstant;
import com.fastx.ai.llm.domains.dto.KnowledgeBaseFileDTO;
import com.fastx.ai.llm.domains.dto.PageDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author stark
 */
@Slf4j
@Component
public class KnowledgeBaseTaskRunner {

    @DubboReference
    IDubboKnowledgeBaseService knowledgeBaseService;

    /**
     * this task used for knowledge base task, like file embedding, image embedding, etc.
     * which will be executed by a fixed rate scheduler.
     */
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void scheduleFixedRunnerForKnowledgeBaseTask() {
        log.info("scheduleFixedRunnerForKnowledgeBaseTask::started");
        startTaskExec(1L, 5L, IConstant.WAIT);
        log.info("scheduleFixedRunnerForKnowledgeBaseTask::finished full search.");
    }

    public void startTaskExec(Long page, Long size, String status) {
        PageDTO<KnowledgeBaseFileDTO> knowledgeBaseFiles =
                knowledgeBaseService.getKnowledgeBaseByPage(page, size, status);

        if (knowledgeBaseFiles.getList().isEmpty()) {
            return ;
        }

        for (KnowledgeBaseFileDTO t : knowledgeBaseFiles.getList()) {
            log.info("scheduleFixedRunnerForKnowledgeBaseTask::taskExec: knowledge: {}, file: {}",
                    t.getKnowledgeBaseId(), t.getId());
            try {
                // query task
                KnowledgeBaseFileDTO knowledgeBaseFile = knowledgeBaseService.getKnowledgeBaseFile(t.getId());
                // check task status
                if (!IConstant.WAIT.equals(knowledgeBaseFile.getStatus())) {
                    continue;
                }
                // Add a task for this file.
                // @TODO (stark) change this knowledge base file status. (domain has redis lock!)
                // @TODO (stark) process knowledge base file.
                // @TODO (stark) need a workflow defined.
            } catch (Exception e) {
                if (StringUtils.contains(e.getMessage().toLowerCase(), "lock")) {
                    // ignored.
                    log.info("scheduleFixedRunnerForKnowledgeBaseTask::updateTaskExec failed", e);
                    continue ;
                }
                log.error("scheduleFixedRunnerForKnowledgeBaseTask:: meet exception", e);
            }
        }
    }

}
