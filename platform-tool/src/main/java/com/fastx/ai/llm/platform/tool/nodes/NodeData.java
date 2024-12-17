package com.fastx.ai.llm.platform.tool.nodes;

import java.util.Map;

/**
 * @author stark
 */
public class NodeData {

    private String name;
    private String label;
    private NodeTool tool;
    private NodeOrganizationTool orgTool;
    private Map<String, Object> innerData;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public NodeTool getTool() {
        return tool;
    }

    public void setTool(NodeTool tool) {
        this.tool = tool;
    }

    public NodeOrganizationTool getOrgTool() {
        return orgTool;
    }

    public void setOrgTool(NodeOrganizationTool orgTool) {
        this.orgTool = orgTool;
    }

    public Map<String, Object> getInnerData() {
        return innerData;
    }

    public void setInnerData(Map<String, Object> innerData) {
        this.innerData = innerData;
    }
}

/**
 * {
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
 *     }
 */

/**
 * "innerData": {
 * 				"inputs": {
 * 					"modelId": "start.outputs.modelId",
 * 					"messages": [{
 * 						"role": "start.outputs[0].role",
 * 						"content": "start.outputs[0].content"
 * 					                    }]                *            }
 * 			}
 */

