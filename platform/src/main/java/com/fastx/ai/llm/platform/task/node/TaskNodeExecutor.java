package com.fastx.ai.llm.platform.task.node;

import com.fastx.ai.llm.platform.exception.TaskExecException;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @author stark
 *
 * indicated that: this executor only for task execute.
 *
 */
@Component
public class TaskNodeExecutor {

    private static Logger log = LoggerFactory.getLogger(TaskNodeExecutor.class);

    private static int cpuNum = Runtime.getRuntime().availableProcessors();

    private static Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (t, e) -> {
        log.error("task exec error: {} ", e.getMessage());
        // we need back error state to task.
    };

    private static ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder()
            // be clarified that the thread is not daemon
            // so, need to avoid resource leak
            .setDaemon(false)
            .setPriority(Thread.NORM_PRIORITY)
            .setNameFormat("task-exec-pool-%d")
            .setUncaughtExceptionHandler(uncaughtExceptionHandler)
            .build();

    private static LinkedBlockingQueue TASK_QUEUE = new LinkedBlockingQueue<>(10);

    private static ExecutorService TASK_EXECUTOR = new ThreadPoolExecutor(
            10,
            10,
            15,
            TimeUnit.MINUTES,
            TASK_QUEUE,
            THREAD_FACTORY,
            new ThreadPoolExecutor.AbortPolicy()
    );

    public void execute(TaskNodeRunnable taskNodeRunnable) {
        if (!canSubmit()) {
            throw new TaskExecException("queue was full!");
        }
        TASK_EXECUTOR.execute(taskNodeRunnable);
    }

    public boolean canSubmit() {
        return TASK_QUEUE.size() < 5;
    }

    @PreDestroy
    public void shutdown() {
        TASK_EXECUTOR.shutdown();
    }

}
