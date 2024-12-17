package com.fastx.ai.llm.platform.tool.nodes;

/**
 * @author stark
 */
public class Edge {
    private String id;
    private String type;
    private String source;
    private String target;
    private String sourceHandle;
    private String targetHandle;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSourceHandle() {
        return sourceHandle;
    }

    public void setSourceHandle(String sourceHandle) {
        this.sourceHandle = sourceHandle;
    }

    public String getTargetHandle() {
        return targetHandle;
    }

    public void setTargetHandle(String targetHandle) {
        this.targetHandle = targetHandle;
    }
}

/**
 *      {
 * 		"id": "xy-edge__1ffde418-ea59-4a74-a8eb-e50253e78b3aoutput-b9505d06-588b-4115-868c-7577caf31cfeinput",
 * 		"type": "simpleEdge",
 * 		"source": "1ffde418-ea59-4a74-a8eb-e50253e78b3a",
 * 		"target": "b9505d06-588b-4115-868c-7577caf31cfe",
 * 		"animated": true,
 * 		"sourceHandle": "output",
 * 		"targetHandle": "input"
 * 	    }
 */