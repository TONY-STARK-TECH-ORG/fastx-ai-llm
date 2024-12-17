package com.fastx.ai.llm.platform.tool.nodes;

import com.alibaba.fastjson2.JSON;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stark
 */
public class WorkflowGraph {

    private List<Node> nodes;
    private List<Edge> edges;

    public static WorkflowGraph of(String jsonData) {
        return JSON.parseObject(jsonData, WorkflowGraph.class);
    }

    public Node findStartNode() {
        try {
            return nodes.stream().filter(node -> node.getData().getTool().getCode().endsWith(".start")).findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Node findEndNode() {
        try {
            return nodes.stream().filter(node -> node.getData().getTool().getCode().endsWith(".end")).findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Node> findNextExecuteNodes(Node currentNode) {
        try {
            List<String> nextNodeIds = List.of();
            for (Edge edge : edges) {
                if (edge.getSource().equals(currentNode.getId())) {
                    nextNodeIds.add(edge.getTarget());
                }
            }
            return nodes.stream().filter(node -> nextNodeIds.contains(node.getId())).toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public List<Edge> findInnerEdges(Node currentNode) {
        try {
            List<Edge> prevEdges = new ArrayList<>();
            edges.forEach(edge -> {
                if (edge.getTarget().equals(currentNode.getId())) {
                    prevEdges.add(edge);
                }
            });
            return prevEdges;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Edge> findOuterEdges(Node currentNode) {
        try {
            List<Edge> nextEdges = new ArrayList<>();
            edges.forEach(edge -> {
                if (edge.getSource().equals(currentNode.getId())) {
                    nextEdges.add(edge);
                }
            });
            return nextEdges;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Node getNode(String nodeId) {
        try {
            return nodes.stream().filter(node -> node.getId().equals(nodeId)).findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNextNodeId(String nId) {
        MutableGraph<String> graph = GraphBuilder.directed().build();
        nodes.forEach(node -> graph.addNode(node.getId()));
        edges.forEach(edge -> graph.putEdge(edge.getSource(), edge.getTarget()));
        // get next nodes and nextNode's parent. execute this.
        return graph.successors(nId).size() > 0 ? graph.successors(nId).iterator().next() : null;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public static void main(String[] args) {
        WorkflowGraph graph = WorkflowGraph.of("{\"edges\": [{\"id\": \"xy-edge__dd7c30e5-cfd1-4afc-b561-5bf7030449b7output-1ffde418-ea59-4a74-a8eb-e50253e78b3ainput\", \"type\": \"simpleEdge\", \"source\": \"dd7c30e5-cfd1-4afc-b561-5bf7030449b7\", \"target\": \"1ffde418-ea59-4a74-a8eb-e50253e78b3a\", \"animated\": true, \"sourceHandle\": \"output\", \"targetHandle\": \"input\"}, {\"id\": \"xy-edge__1ffde418-ea59-4a74-a8eb-e50253e78b3aoutput-b9505d06-588b-4115-868c-7577caf31cfeinput\", \"type\": \"simpleEdge\", \"source\": \"1ffde418-ea59-4a74-a8eb-e50253e78b3a\", \"target\": \"b9505d06-588b-4115-868c-7577caf31cfe\", \"animated\": true, \"sourceHandle\": \"output\", \"targetHandle\": \"input\"}], \"nodes\": [{\"id\": \"1ffde418-ea59-4a74-a8eb-e50253e78b3a\", \"data\": {\"name\": \"llm\", \"tool\": {\"code\": \"openai.chat\", \"icon\": \"https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png\", \"name\": \"OpenAI\", \"type\": \"llm-model\", \"author\": \"fastx-ai\", \"status\": \"active\", \"version\": \"1.0.0\", \"prototype\": \"{\\\"config\\\":[{\\\"array\\\":false,\\\"name\\\":\\\"apiKey\\\",\\\"required\\\":true,\\\"type\\\":\\\"java.lang.String\\\",\\\"value\\\":\\\"\\\"},{\\\"array\\\":false,\\\"name\\\":\\\"baseUrl\\\",\\\"required\\\":true,\\\"type\\\":\\\"java.lang.String\\\",\\\"value\\\":\\\"https://api.openai.com/v1\\\"},{\\\"array\\\":false,\\\"name\\\":\\\"streaming\\\",\\\"required\\\":true,\\\"type\\\":\\\"java.lang.Boolean\\\",\\\"value\\\":\\\"\\\"}],\\\"inputs\\\":[{\\\"array\\\":false,\\\"name\\\":\\\"modelId\\\",\\\"required\\\":true,\\\"type\\\":\\\"java.lang.String\\\",\\\"value\\\":\\\"gpt-4o-mini\\\"},{\\\"array\\\":true,\\\"name\\\":\\\"messages\\\",\\\"required\\\":true,\\\"type\\\":\\\"com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIMessage\\\",\\\"value\\\":\\\"\\\"}],\\\"outputs\\\":[{\\\"array\\\":false,\\\"name\\\":\\\"id\\\",\\\"required\\\":true,\\\"type\\\":\\\"java.lang.String\\\",\\\"value\\\":\\\"\\\"},{\\\"array\\\":false,\\\"name\\\":\\\"created\\\",\\\"required\\\":true,\\\"type\\\":\\\"java.lang.Long\\\",\\\"value\\\":\\\"\\\"},{\\\"array\\\":true,\\\"name\\\":\\\"choices\\\",\\\"required\\\":true,\\\"type\\\":\\\"com.openai.models.ChatCompletion$Choice\\\",\\\"value\\\":\\\"\\\"},{\\\"array\\\":false,\\\"name\\\":\\\"usage\\\",\\\"required\\\":true,\\\"type\\\":\\\"com.openai.models.CompletionUsage\\\",\\\"value\\\":\\\"\\\"}]}\", \"description\": \"OpenAI Chat工具，支持流、非流调用全部会话模型接口。\"}, \"label\": \"llm-model node\", \"orgTool\": {\"id\": 1, \"key\": 1, \"custom\": false, \"toolDTO\": null, \"toolCode\": \"openai.chat\", \"configData\": \"{\\\"organizationId\\\":1,\\\"baseUrl\\\":\\\"https://goapi.gptnb.ai/v1\\\",\\\"streaming\\\":\\\"true\\\",\\\"apiKey\\\":\\\"******\\\"}\", \"createTime\": \"2024-12-15 01:01:08\", \"customCode\": null, \"updateTime\": \"2024-12-15 01:01:08\", \"toolVersion\": \"1.0.0\", \"organizationId\": 1}, \"innerData\": {\"inputs\": {\"modelId\": \"start.outputs.modelId\", \"messages\": [{\"role\": \"start.outputs[0].role\", \"content\": \"start.outputs[0].content\"}]}}}, \"type\": \"llm-model\", \"dragging\": false, \"measured\": {\"width\": 253, \"height\": 242}, \"position\": {\"x\": 303, \"y\": 51}, \"selected\": false}, {\"id\": \"dd7c30e5-cfd1-4afc-b561-5bf7030449b7\", \"data\": {\"name\": \"start\", \"tool\": {\"code\": \"llm.start\", \"icon\": \"https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png\", \"name\": \"LLM StartNode\", \"type\": \"llm-function\", \"author\": \"fastx-ai\", \"status\": \"active\", \"version\": \"1.0.0\", \"prototype\": \"{\\\"config\\\":[],\\\"inputs\\\":[{\\\"array\\\":false,\\\"name\\\":\\\"modelId\\\",\\\"required\\\":true,\\\"type\\\":\\\"java.lang.String\\\",\\\"value\\\":\\\"gpt-4o-mini\\\"},{\\\"array\\\":true,\\\"name\\\":\\\"messages\\\",\\\"required\\\":true,\\\"type\\\":\\\"com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIMessage\\\",\\\"value\\\":\\\"\\\"}],\\\"outputs\\\":[{\\\"array\\\":false,\\\"name\\\":\\\"modelId\\\",\\\"required\\\":true,\\\"type\\\":\\\"java.lang.String\\\",\\\"value\\\":\\\"gpt-4o-mini\\\"},{\\\"array\\\":true,\\\"name\\\":\\\"messages\\\",\\\"required\\\":true,\\\"type\\\":\\\"com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIMessage\\\",\\\"value\\\":\\\"\\\"}]}\", \"description\": \"LLM input node. user input, system input and more ...\"}, \"label\": \"llm-function node\", \"orgTool\": {\"id\": 3, \"key\": 3, \"custom\": false, \"toolDTO\": null, \"toolCode\": \"llm.start\", \"configData\": \"{\\\"organizationId\\\":1}\", \"createTime\": \"2024-12-17 06:08:40\", \"customCode\": null, \"updateTime\": \"2024-12-17 06:08:40\", \"toolVersion\": \"1.0.0\", \"organizationId\": 1}, \"innerData\": {\"inputs\": {\"modelId\": \"gpt-4o-mini\", \"messages\": [{\"role\": \"user\", \"content\": \"hi\"}, {\"role\": \"user\", \"content\": \"hi\"}]}}}, \"type\": \"llm-function\", \"dragging\": false, \"measured\": {\"width\": 253, \"height\": 344}, \"position\": {\"x\": 0, \"y\": 0}, \"selected\": false}, {\"id\": \"b9505d06-588b-4115-868c-7577caf31cfe\", \"data\": {\"name\": \"end\", \"tool\": {\"code\": \"llm.end\", \"icon\": \"https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png\", \"name\": \"LLM EndNode\", \"type\": \"llm-function\", \"author\": \"fastx-ai\", \"status\": \"active\", \"version\": \"1.0.0\", \"prototype\": \"{\\\"config\\\":[],\\\"inputs\\\":[{\\\"array\\\":false,\\\"name\\\":\\\"modelId\\\",\\\"required\\\":true,\\\"type\\\":\\\"java.lang.String\\\",\\\"value\\\":\\\"gpt-4o-mini\\\"},{\\\"array\\\":true,\\\"name\\\":\\\"messages\\\",\\\"required\\\":true,\\\"type\\\":\\\"com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIMessage\\\",\\\"value\\\":\\\"\\\"}],\\\"outputs\\\":[{\\\"array\\\":false,\\\"name\\\":\\\"modelId\\\",\\\"required\\\":true,\\\"type\\\":\\\"java.lang.String\\\",\\\"value\\\":\\\"gpt-4o-mini\\\"},{\\\"array\\\":false,\\\"name\\\":\\\"content\\\",\\\"required\\\":true,\\\"type\\\":\\\"java.lang.String\\\",\\\"value\\\":\\\"\\\"}]}\", \"description\": \"LLM output node. assistant output, system output and more ...\"}, \"label\": \"llm-function node\", \"orgTool\": {\"id\": 4, \"key\": 4, \"custom\": false, \"toolDTO\": null, \"toolCode\": \"llm.end\", \"configData\": \"{\\\"organizationId\\\":1}\", \"createTime\": \"2024-12-17 06:32:50\", \"customCode\": null, \"updateTime\": \"2024-12-17 06:32:50\", \"toolVersion\": \"1.0.0\", \"organizationId\": 1}, \"innerData\": {\"inputs\": {\"modelId\": \"llm.modelId\", \"messages\": [{\"role\": \"llm.outputs.choices[0].message.role\", \"content\": \"llm.outputs.choices[0].message.content\"}]}}}, \"type\": \"llm-function\", \"dragging\": false, \"measured\": {\"width\": 253, \"height\": 262}, \"position\": {\"x\": 606, \"y\": 41}, \"selected\": false}]}");
        System.out.println(graph.getNextNodeId("1ffde418-ea59-4a74-a8eb-e50253e78b3a"));
    }
}

/**
 * {
 *   "nodes": [],
 *   "edges": []
 * }
 */
