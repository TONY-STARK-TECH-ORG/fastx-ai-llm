package com.fastx.ai.llm.platform.exec;

import com.fastx.ai.llm.platform.tool.llm.LLMOutput;
import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;

/**
 * @author stark
 */
public class ToolRunner implements Runnable {

    @Override
    public void run() {
        ToolContext toolContext = PlatformToolExecutor.EXEC_CONTEXT.get();
        try {
            // exec
            IPlatformToolOutput exec = toolContext.getTool().exec(toolContext.getLlmInput());
            // set output
            toolContext.setLlmOutput(exec);
        } catch (Exception e) {
            toolContext.setLlmOutput(LLMOutput.ofError(e.getMessage()));
        }
    }
}
