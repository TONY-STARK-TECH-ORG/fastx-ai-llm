package com.fastx.ai.llm.platform.in.exec.workflow;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author stark
 */
@Slf4j
@Component
public class WorkflowInContextHolder {
    public static TransmittableThreadLocal<WorkflowInContext> CONTEXT = new TransmittableThreadLocal<>();

    public static void setContext(WorkflowInContext context) {
        CONTEXT.set(context);
    }

    public static WorkflowInContext getContext() {
        return CONTEXT.get();
    }

    public static void removeContext() {
        CONTEXT.remove();
    }
}
