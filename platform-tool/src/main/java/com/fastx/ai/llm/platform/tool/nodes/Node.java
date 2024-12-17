package com.fastx.ai.llm.platform.tool.nodes;

/**
 * @author stark
 */
public class Node {

    private String id;
    private String type;
    private NodeData data;
    private boolean executed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NodeData getData() {
        return data;
    }

    public void setData(NodeData data) {
        this.data = data;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }
}

/**
 * "id": "1ffde418-ea59-4a74-a8eb-e50253e78b3a",
 *     "data": {
 *       "name": "llm",
 *       "tool": {
 *         "code": "openai.chat",
 *         "icon": "https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png",
 *         "name": "OpenAI",
 *         "type": "llm-model",
 *         "author": "fastx-ai",
 *         "status": "active",
 *         "version": "1.0.0",
 *         "prototype": "{\"config\":[{\"array\":false,\"name\":\"apiKey\",\"required\":true,\"type\":\"java.lang.String\",\"value\":\"\"},{\"array\":false,\"name\":\"baseUrl\",\"required\":true,\"type\":\"java.lang.String\",\"value\":\"https://api.openai.com/v1\"},{\"array\":false,\"name\":\"streaming\",\"required\":true,\"type\":\"java.lang.Boolean\",\"value\":\"\"}],\"inputs\":[{\"array\":false,\"name\":\"modelId\",\"required\":true,\"type\":\"java.lang.String\",\"value\":\"gpt-4o-mini\"},{\"array\":true,\"name\":\"messages\",\"required\":true,\"type\":\"com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIMessage\",\"value\":\"\"}],\"outputs\":[{\"array\":false,\"name\":\"id\",\"required\":true,\"type\":\"java.lang.String\",\"value\":\"\"},{\"array\":false,\"name\":\"created\",\"required\":true,\"type\":\"java.lang.Long\",\"value\":\"\"},{\"array\":true,\"name\":\"choices\",\"required\":true,\"type\":\"com.openai.models.ChatCompletion$Choice\",\"value\":\"\"},{\"array\":false,\"name\":\"usage\",\"required\":true,\"type\":\"com.openai.models.CompletionUsage\",\"value\":\"\"}]}",
 *         "description": "OpenAI Chat工具，支持流、非流调用全部会话模型接口。"
 *       },
 *       "label": "llm-model node",
 *       "orgTool": {
 *         "id": 1,
 *         "key": 1,
 *         "custom": false,
 *         "toolDTO": null,
 *         "toolCode": "openai.chat",
 *         "configData": "{\"organizationId\":1,\"baseUrl\":\"https://goapi.gptnb.ai/v1\",\"streaming\":\"true\",\"apiKey\":\"******\"}",
 *         "createTime": "2024-12-15 01:01:08",
 *         "customCode": null,
 *         "updateTime": "2024-12-15 01:01:08",
 *         "toolVersion": "1.0.0",
 *         "organizationId": 1
 *       }
 *     },
 *     "type": "llm-model",
 *     "dragging": false,
 *     "measured": {
 *       "width": 247,
 *       "height": 160
 *     },
 *     "position": {
 *       "x": 303,
 *       "y": 92
 *     },
 *     "selected": false
 */