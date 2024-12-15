import {Tabs, List, Card, Spin, message, Badge, Popconfirm, Button} from 'antd';
import {useLocation, useNavigate} from "react-router";
import {useEffect, useState} from "react";
import {http} from "../../api/Http.ts";
import {OrgTask} from "../../store/define.ts";
import TaskLogDrawer from "../../components/dashboard/TaskLogDrawer.tsx";
import {DeleteOutlined} from "@ant-design/icons";
import PageIllustrationDashboard from "../../components/page-illustration-dashboard.tsx";
import {useOrganizationStore} from "../../store/OrganizationStore.ts";

type TaskSepDataMap = {
    [key: string]: OrgTask[]
}

type TaskTaps = {
    name: string;
    key: string;
}

export default function TaskPage() {
    // handle location change.
    const location = useLocation();
    const [currentLocation, setCurrentLocation] = useState<string | undefined>()

    const navigate = useNavigate();
    const [loadingData, setLoadingData] = useState(false);

    const [orgId] = useOrganizationStore(state => [state.id])

    // log drawer
    const [taskLogDrawerOpen, setTaskDrawerOpen] = useState(false)
    const [selectedTask, setSelectedTask] = useState<OrgTask | undefined>(undefined)

    // datas
    const [taskDataMap, setTaskDataMap] = useState<TaskSepDataMap | undefined>({
        "knowledge": [],
        "cron": [],
        "train": [],
        "custom": [],
        "other": [],
    })

    const [tabKey] = useState<TaskTaps[] | undefined>([
        { name: "知识库任务", key: "knowledge" },
        { name: "定时任务", key: "cron" },
        { name: "模型训练任务", key: "train" },
        { name: "自定义任务", key: "custom" },
        { name: "其他", key: "other" }
    ])

    // return data for tab.
    const getChildrenData = () => {
        const path = location.pathname.replace("/dashboard/task/", "");
        if (!taskDataMap) {
            return []
        }
        return taskDataMap[path]
    }

    const getChildren = () => {
        return orgId === undefined ? (
                    <div className="w-full h-full bg-white items-center justify-center flex">
                        <PageIllustrationDashboard />
                        <div className="mx-auto max-w-6xl px-4 sm:px-6">
                            {/* Hero content */}
                            <div className="flex flex-col items-center justify-center">
                                {/* Section header */}
                                <div className="text-center">
                                    <h1
                                        className="border-y text-5xl font-bold [border-image:linear-gradient(to_right,transparent,theme(colors.slate.300/.8),transparent)1] md:text-6xl"
                                    >
                                        <span className="text-green-500">TASK</span><span className="text-pink-600 ml-6">CENTER</span>
                                    </h1>
                                </div>
                                <div>
                                    <h1 className="text-xl font-normal mt-3">
                                        请选择一个组织开始吧!
                                    </h1>
                                </div>
                            </div>
                        </div>
                    </div>
                ) : (
                    <div className="w-full h-full bg-white overflow-auto p-2">
                        <List
                            grid={{ gutter: 24, column: 4 }}
                            dataSource={getChildrenData()}
                            renderItem={(item) => (
                                <List.Item>
                                    <Badge.Ribbon text={item.status === 'active' ? "运行中" : "未运行"}
                                                  color={item.status === 'active' ? "green" : "orange"}>
                                        <Card
                                            className="rounded-[4px] hover:shadow-sm hover:shadow-[#FF6A00]/20"
                                            styles={{
                                                body: {
                                                    padding: 0
                                                }
                                            }}
                                        >
                                            <div
                                                className="flex justify-center items-center p-2 flex-col items-center justify-start">
                                                <div
                                                    onClick={() => {
                                                        setSelectedTask(item)
                                                        setTaskDrawerOpen(true)
                                                    }}
                                                    className="w-full flex items-center hover:cursor-pointer">
                                                    <div className="flex flex-col items-start justify-center">
                                                        <p className="text-[15px] text-black">{item.name}</p>
                                                        <p className="text-[10px] text-black/50">最后更新：{item.updateTime}</p>
                                                    </div>
                                                </div>
                                                <div className="flex items-center w-full">
                                                    <div className="grow">
                                                        <div className="w-full flex items-start justify-start mt-2">
                                                            <p className="text-[12px] text-left">{item.description}</p>
                                                        </div>
                                                        <div className="w-full flex items-start justify-start">
                                                            <p className="text-[12px] text-left">
                                                                运行策略：
                                                                <span
                                                                    className="ml-0.5 font-bold text-[#FF6A00]">{item.cron === '-1' ? '运行一次' : ("定时: " + item.cron)}
                                                </span>
                                                            </p>
                                                        </div>
                                                    </div>
                                                    <div>
                                                        <Popconfirm
                                                            title="删除任务"
                                                            description="删除任务会同步删除所有任务记录，确定删除吗？"
                                                            onConfirm={() => {
                                                                deleteTask(item)
                                                            }}
                                                            onCancel={() => {}}
                                                            okText="是的"
                                                            cancelText="取消"
                                                        >
                                                            <Button type={"text"} icon={<DeleteOutlined className="text-red-600" />}></Button>
                                                        </Popconfirm>
                                                    </div>
                                                </div>
                                            </div>
                                        </Card>
                                    </Badge.Ribbon>
                                </List.Item>
                            )}
                        />
                    </div>
                )
    }

    // load tools from server
    const loadOrgTasks = async () => {
        setLoadingData(true);
        const res = await http.get<OrgTask[]>("task/org/task/list?orgId=" + orgId)
        if (res.success) {
            // set to sep tools data
            const map: TaskSepDataMap = {
                "knowledge": [],
                "cron": [],
                "train": [],
                "custom": [],
                "other": [],
            }
            const types = ["knowledge", "cron", "train", "custom"];
            res.data?.forEach((t) => {
                if (types.indexOf(t.type) != -1) {
                    map[t.type].push(t)
                } else {
                    map['other'].push(t)
                }
            })
            setTaskDataMap(map)
        } else {
            message.error("加载任务列表出错，请刷新页面重试")
        }
        setLoadingData(false);
    }

    const deleteTask = async (task: OrgTask) => {
        setLoadingData(true);
        const res = await http.post<OrgTask>("task/org/task/delete", {
            id: task.id
        })
        if (res.success) {
            message.success("删除任务成功")
            setLoadingData(false);
            loadOrgTasks()
        } else {
            message.error("删除任务失败，请刷新页面重试: " + res.msg)
            setLoadingData(false);
        }
    }

    useEffect(() => {
        if (orgId) {
            loadOrgTasks()
        }
        setCurrentLocation(location.pathname.replace("/dashboard/task/", ""))
    }, [location, orgId]);

    return (
        <div className="w-full h-full p-2 flex flex-col">
            <Tabs
                defaultActiveKey={currentLocation}
                activeKey={currentLocation}
                type="card"
                size="small"
                tabBarStyle={{margin: 0}}
                items={tabKey?.map((item) => {
                    return {
                        label: item.name,
                        key: item.key
                    };
                })}
                onChange={(key) => {
                    navigate("/dashboard/task/" + key)
                }}
            />
            <div className="w-full grow">
                {getChildren()}
            </div>
            <Spin fullscreen spinning={loadingData} />
            <TaskLogDrawer open={taskLogDrawerOpen} current={selectedTask} onClose={() => {
                setTaskDrawerOpen(false)
            }} />
        </div>
    )
}