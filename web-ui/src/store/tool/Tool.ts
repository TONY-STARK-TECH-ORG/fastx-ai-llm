import {Response} from "../define.ts";

export interface Tool {
    code: string;
    version: string;
    type: string;
    author: string;
    status: string;

    key?: string;
}

export interface ToolAction {
    getTools: () => Promise<Response<Tool[]>>;
}