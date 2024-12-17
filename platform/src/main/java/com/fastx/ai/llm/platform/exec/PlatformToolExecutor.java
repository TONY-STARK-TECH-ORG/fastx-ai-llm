package com.fastx.ai.llm.platform.exec;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import com.fastx.ai.llm.platform.tool.llm.LLMOutput;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author stark
 *
 * indicated that: this executor only for LLM Streaming execute.
 *
 */
@Component
public class PlatformToolExecutor {

    private static Logger log = LoggerFactory.getLogger(PlatformToolExecutor.class);

    private static ExecutorService TOOL_EXECUTOR;

    private static ThreadFactory THREAD_FACTORY;

    private static Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (t, e) -> {
        if (Objects.nonNull(PlatformToolExecutor.EXEC_CONTEXT.get())) {
            PlatformToolExecutor.EXEC_CONTEXT.get().setLlmOutput(LLMOutput.of(e.getMessage()));
        }
        log.error("tool exec error: {} ", e.getMessage());
    };

    public static TransmittableThreadLocal<ToolContext> EXEC_CONTEXT = new TransmittableThreadLocal<>();

    static {
        // get cup num
        int cpuNum = Runtime.getRuntime().availableProcessors();
        // create thread factory
        THREAD_FACTORY = new ThreadFactoryBuilder()
                // be clarified that the thread is not daemon
                // so, need to avoid resource leak
                .setDaemon(false)
                .setPriority(Thread.NORM_PRIORITY)
                .setNameFormat("tool-exec-pool-%d")
                .setUncaughtExceptionHandler(uncaughtExceptionHandler)
                .build();

        TOOL_EXECUTOR = new ThreadPoolExecutor(
                cpuNum * 2,
                cpuNum * 4,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024),
                THREAD_FACTORY,
                // whe thread pool is full, run the task in the current thread
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public void execute(ToolRunner toolRunner) {
        // wrap runnable with ttl, send value to child thread.
        Runnable ttlRunnable = TtlRunnable.get(toolRunner);
        TOOL_EXECUTOR.execute(ttlRunnable);
    }

    public void setContext(ToolContext context) {
        PlatformToolExecutor.EXEC_CONTEXT.set(context);
    }

    public void remove() {
        PlatformToolExecutor.EXEC_CONTEXT.remove();
    }

    @PreDestroy
    public void shutdown() {
        TOOL_EXECUTOR.shutdown();
    }

}
