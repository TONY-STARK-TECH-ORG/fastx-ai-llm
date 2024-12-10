package com.fastx.ai.llm.platform.tool.llm.model;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.TypeUtils;
import com.fastx.ai.llm.platform.tool.entity.Fields;
import com.fastx.ai.llm.platform.tool.llm.BaseLlmTool;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author stark
 */
public abstract class BaseLlmModel extends BaseLlmTool {

    @Override
    public String getType() {
        return "llm-model";
    }

    public static String CONFIG = "config";
    public static String INPUTS = "inputs";

    public Object parseFields(Map<String, Map<String, JSONPath>> cache, String key, String data, Fields fields) {
        JSONPath path;
        if (cache.containsKey(key) && cache.get(key).containsKey(fields.getName())) {
            path = cache.get(key).get(fields.getName());
        } else {
            JSONPath.Feature[] futures = {fields.isRequired() ? JSONPath.Feature.NullOnError : JSONPath.Feature.KeepNullValue};
            Type parseType = fields.isArray() ? TypeUtils.getArrayClass(fields.getType()) : fields.getType();
            path = JSONPath.of("$." + fields.getName(), parseType, futures);
            cache.get(key).put(fields.getName(), path);
        }
        JSONReader parser = JSONReader.of(data);
        return path.extract(parser);
    }

}
