import { Table } from "antd";
import {Tool} from "../store/tool/Tool.ts"
import {http} from "../api/Http.ts";
import {useEffect, useState} from "react";

export default function Tools() {
    const [dataSource, setDataSource] = useState<Tool[] | undefined>([])
    const columns = [
        {
            title: "标识",
            dataIndex: "code",
            key: "code"
        },
        {
            title: "版本号",
            dataIndex: "version",
            key: "version"
        },
        {
            title: "类型",
            dataIndex: "type",
            key: "type"
        },
        {
            title: "创建人",
            dataIndex: "author",
            key: "author"
        },
        {
            title: "状态",
            dataIndex: "status",
            key: "status"
        },
    ]
    useEffect(() => {
        http.get<Tool[]>('tool/platform/tool/list').then(resp => {
            resp.data?.forEach(item => {
                item.status = item.status === 'active' ? '可用' : '不可用';
                item.key = item.code + "|" + item.version + "|" + item.type;
            })
            setDataSource(resp.data)
        })
    }, [])
    return (
        <div className="w-full">
            <Table dataSource={dataSource} columns={columns}/>
        </div>
    )
}