package com.fastx.ai.llm.platform.exec.workflow;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author stark
 */
@Slf4j
@Component
public class WorkflowExecuteContext {
    public static TransmittableThreadLocal<WorkflowContext> CONTEXT = new TransmittableThreadLocal<>();

    public static void setContext(WorkflowContext context) {
        CONTEXT.set(context);
    }

    public static WorkflowContext getContext() {
        return CONTEXT.get();
    }

    public static void removeContext() {
        CONTEXT.remove();
    }
}
