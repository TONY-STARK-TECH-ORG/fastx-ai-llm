package com.fastx.ai.llm.domains.config;

import com.p6spy.engine.spy.appender.StdoutLogger;

/**
 * @author stark
 */
public class SqlDebugLogger extends StdoutLogger {

    @Override
    public void logText(String text) {
        System.err.println(text);
    }

}
