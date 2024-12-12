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