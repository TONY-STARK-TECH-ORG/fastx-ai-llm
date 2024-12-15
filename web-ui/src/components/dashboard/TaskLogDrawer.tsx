import {useEffect, useState} from 'react';
import {Button, Drawer, message, Space, Table} from 'antd';
import type { TableColumnsType } from 'antd';
import {OrgTask, OrgTaskLog, Page} from "../../store/define.ts";
import ReactJsonView from '@microlink/react-json-view'
import {http} from "../../api/Http.ts";
import {StateMapping} from "../../utils/StateMapping.ts";

type TaskLogDrawerProps = {open:boolean; current: OrgTask | undefined; onClose: ()=>void }

const columns: TableColumnsType<OrgTaskLog> = [
    { align: 'center', title: '编号', dataIndex: 'id', key: 'id' },
    { align: 'center', title: '创建时间', dataIndex: 'createTime', key: 'createTime' },
    { align: 'center', title: '更新时间', dataIndex: 'updateTime', key: 'updateTime' },
    { align: 'center', title: '任务状态', dataIndex: 'status', key: 'status', render: (status) =>
            <p className={"text-[" + StateMapping.getTaskStatusColor(status) + "]"}>{StateMapping.getTaskStatusZh(status)}</p>},
    { align: 'center', title: '结束时间', dataIndex: 'completeTime', key: 'completeTime' },
];

export default function TaskLogDrawer(
    {open, current, onClose}:
    TaskLogDrawerProps
) {
    const [total, setTotal] = useState(0)
    const [page, setPage] = useState(1)
    const [size, setSize] = useState(20)

    const [logDataList, setLogDataList] = useState<OrgTaskLog[] | undefined>([])
    const [loading, setLoading] = useState(false)
    // load task log.
    const loadTaskLog = async (page: number, size: number) => {
        setLoading(true)
        // Long taskId, Long page, Long size, String status
        const res = await http.getWithParams<Page<OrgTaskLog>>("task/org/task/log/list", {
            taskId: current?.id,
            page: page,
            size: size,
            status: ""
        })
        if (res.success) {
            setTotal(res.data?.total!!)
            res.data?.list.forEach(d => d.key = d.id!!)
            setLogDataList(res.data?.list)
        } else {
            message.error("加载任务记录失败，请刷新后重试: " + res.msg)
        }
        setLoading(false)
    }
    // run current task once.
    const createTaskLogManually = async () => {
        setLoading(true)
        const res = await http.post<OrgTaskLog>("task/org/task/log/create", {
            taskId: current?.id
        })
        if (res.success) {
            message.success("手动触发任务成功")
            setLoading(false)
            loadTaskLog(page, size)
        } else {
            message.error("手动触发任务失败，请刷新后重试: " + res.msg)
            setLoading(false)
        }
    }

    useEffect(() => {
        if (open && current) {
            loadTaskLog(page, size)
        }
    }, [current, open]);

    return (
        <Drawer
            width="50%"
            title={current?.name}
            onClose={onClose}
            open={open}
            styles={{
                body: {
                    padding: 8
                },
            }}
            extra={
                <Space>
                    <Button onClick={onClose}>关闭</Button>
                    <Button onClick={createTaskLogManually} type="primary">手动触发</Button>
                </Space>
            }
        >
            <div className="flex flex-col h-full">
                <Table<OrgTaskLog>
                    loading={loading}
                    columns={columns}
                    expandable={{
                        expandedRowRender: (record) =>
                            (
                                <div className="m-0">
                                    <ReactJsonView src={record}/>
                                </div>
                            ),
                        rowExpandable: () => true,
                    }}
                    scroll={{ y: "calc(100vh - 200px)" }}
                    dataSource={logDataList}
                    pagination={{
                        position: ['bottomRight'],
                        total: total,
                        onChange: (nPage, nSize) => {
                            setPage(nPage)
                            setSize(nSize)
                            loadTaskLog(nPage, nSize)
                        }
                    }}
                />
            </div>
        </Drawer>
    )
}