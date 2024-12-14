import {Tool} from "./tool/Tool.ts";

export interface Base {
    createTime: string;
    id: string;
    updateTime: string;
}

export interface Response<T> {
    code: number;
    msg: string;
    data: T | undefined;
    success: boolean;
}

export interface Token {
    token: string;
}

export interface Organization extends Base {
    name: string
}

export interface Node {
    name: string;
    data: string;
    tool: Tool;
}

export interface WorkflowVersion extends Base {
    workflowId: string;
    version: string;
    status: string;
    nodesData: string;
    nodes: Node[];
}

export interface ApplicationVersion extends Base {
    applicationId: string;
    version: string;
    status: string;
    config: string;
    workflowVersion: WorkflowVersion;
}

export interface Application extends Base {
    name: string;
    type: string;
    description: string;
    iconUrl: string;
    status: string;
    organization: Organization;
    activeVersion: ApplicationVersion
    applicationVersions: ApplicationVersion[]
}