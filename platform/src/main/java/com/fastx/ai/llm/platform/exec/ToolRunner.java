package com.fastx.ai.llm.platform.exec;

import com.fastx.ai.llm.platform.tool.spi.IPlatformToolOutput;

/**
 * @author stark
 */
public class ToolRunner implements Runnable {

    @Override
    public void run() {
        ToolContext toolContext = PlatformToolExecutor.EXEC_CONTEXT.get();
        // exec
        IPlatformToolOutput exec = toolContext.getTool().exec(toolContext.getLlmInput());
        // set output
        toolContext.setLlmOutput(exec);
    }
}
