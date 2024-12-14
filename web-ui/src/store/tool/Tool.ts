import {Response} from "../define.ts";

export interface Tool {
    icon: string;
    name: string;
    description: string;
    code: string;
    version: string;
    type: string;
    author: string;
    status: string;
    prototype: string;
    key?: string;
}

export interface ToolAction {
    getTools: () => Promise<Response<Tool[]>>;
}