import {Tool} from "./tool/Tool.ts";

export interface Base {
    createTime: string;
    id?: string;
    updateTime: string;
}

export interface Response<T> {
    code: number;
    msg: string;
    data: T | undefined;
    success: boolean;
}

export interface Page<T> {
    page: number;
    size: number;
    total: number;
    list: T[];
}

export interface Token {
    token: string;
}

export interface Organization extends Base {
    name?: string
}

export interface Node {
    name: string;
    data: string;
    tool: Tool;
}

export interface Workflow extends Base {
    name: string;
    status: string;
    organizationId: string;
    activeVersion: WorkflowVersion | undefined;
}

export interface WorkflowVersion extends Base {
    workflowId: string;
    version: string;
    status: string;
    versionData: string;
}

export interface WorkFlowExecLog extends Base {
    workflowVersionId: string;
    inputData: string;
    outputData: string;
    execData: string;
    extraData: string;
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

export interface KnowledgeBase extends Base {
    name: string;
    description: string;
    organizationId: string;
    status: string;
}

export interface KnowledgeBaseFile extends Base {
    knowledgeBaseId: string;
    name: string;
    extension: string;
    downloadUrl: string;
    vecCollectionName: string;
    vecCollectionId: string;
    vecPartitionKey: string;
    vecPartitionValue: string;
    status: string;
}

export interface OrgTool extends Base {
    organizationId: string;
    toolCode: string;
    toolVersion: string;
    configData: string;
    custom: boolean;
    customCode: string;
}

export interface OrgTask extends Base {
    name: string;
    description: string;
    organizationId: string;
    cron: string;
    workflowId: string;
    status: string;
    type: string;
}

export interface OrgTaskLog extends Base {
    key: string;
    taskId: string;
    completeTime: string;
    status: string;
    log: string;
}