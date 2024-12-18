package com.fastx.ai.llm.platform.tool.llm.tool.api;

import com.fastx.ai.llm.platform.tool.llm.tool.BaseLlmTools;

/**
 * @author stark
 */
public class WeatherAPI extends BaseLlmTools {

    @Override
    public String getName() {
        return "Weather API";
    }

    @Override
    public String getDescription() {
        return "Weather API: For weather search.";
    }

    @Override
    public String getCode() {
        return "llm.tool.weather";
    }

    @Override
    public String getPrototype() {
        return "";
    }
}
