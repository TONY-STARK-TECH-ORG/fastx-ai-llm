import {Tabs, List, Card, Spin, message, Badge} from 'antd';
import {useLocation, useNavigate} from "react-router";
import {useContext, useEffect, useState} from "react";
import {http} from "../../api/Http.ts";
import {Tool} from "../../store/tool/Tool.ts";
import {UserContext} from "../../context/UserContext.ts";
import {OrgTool} from "../../store/define.ts";
import ToolDetailDrawer from "../../components/dashboard/ToolDetailDrawer.tsx";

type ToolSepDataMap = {
    [key: string]: {
        [key: string]: Tool[]
    }
}

type ToolTaps = {
    name: string;
    key: string;
}

export default function ToolPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const [loadingData, setLoadingData] = useState(false);
    const { user } = useContext(UserContext)
    // datas
    const [toolDataMap, setToolDataMap] = useState<ToolSepDataMap | undefined>({
        "llm": {
            "model": [],
            "function": [],
            "tool": [],
            "other": []
        },
        "train": {
            "pre": [],
            "post": [],
            "tokenizer": [],
            "finetune": [],
            "other": []
        },
    })
    const [tabKey] = useState<ToolTaps[] | undefined>([
        { name: "LLM 模型", key: "llm-model" },
        { name: "LLM 函数", key: "llm-function" },
        { name: "LLM 工具", key: "llm-tool" },
        { name: "LLM 其他", key: "llm-other" },
        { name: "数据预处理", key: "train-dataset" },
        { name: "后处理", key: "train-post" },
        { name: "上传/下载", key: "train-upload" },
        { name: "Tokenizer", key: "train-tokenizer" },
        { name: "微调", key: "train-finetune" },
        { name: "超参数", key: "train-hyperParams" },
        { name: "评测", key: "train-benchmark" },
        { name: "其他", key: "train-other" },
    ])
    const [orgTools, setOrgTools] = useState<OrgTool[] | undefined>([])

    const [toolDetailDrawerOpen, setToolDetailDrawerOpen] = useState(false);
    const [currentToolDetail, setCurrentToolDetail] = useState<Tool | undefined>(undefined)
    const [currentLocation, setCurrentLocation] = useState<string | undefined>()

    const openToolDetail = (item: Tool) => {
        setCurrentToolDetail(item)
        // send all orgTools to model.
        setToolDetailDrawerOpen(true)
    }

    const getChildrenData = () => {
        const path = location.pathname.replace("/dashboard/tool/", "");
        if (path.startsWith("llm-")) {
            const sub = path.replace("llm-", "");
            return toolDataMap?.llm[sub]
        }
        if (path.startsWith("train-")) {
            const sub = path.replace("train-", "");
            return toolDataMap?.train[sub]
        }
    }

    const getChildren = () => {
        return (
            <div className="w-full h-full bg-white overflow-auto p-2">
                <List
                    grid={{ gutter: 24, column: 4 }}
                    dataSource={getChildrenData()}
                    renderItem={(item) => (
                        <List.Item>
                            <Badge.Ribbon text={item.version} color={item.status === 'active' ? "green" : "orange"}>
                                <Card
                                    onClick={() => {
                                        openToolDetail(item);
                                    }}
                                    className="rounded-[4px] hover:cursor-pointer"
                                    styles={{
                                        body: {
                                            padding: 0
                                        }
                                    }}
                                >
                                    <div className={"flex justify-center items-center p-2 flex-col items-center justify-start"}>
                                        <div className="w-full flex items-center">
                                            <img src={item.icon} className={"w-[50px] h-[50px] border border-gray-100"} alt=""/>
                                            <div className="flex flex-col ml-2 items-start justify-center">
                                                <p className="text-[15px] text-black">{item.name}</p>
                                                <p className="text-[10px] text-black/50">作者：{item.author}</p>
                                            </div>
                                        </div>
                                        <div className="flex items-start justify-start mt-2">
                                            <p className="text-[12px] text-left">
                                                <span className={
                                                    (orgTools?.find(o => o.toolCode === item.code && o.toolVersion === item.version) ? "text-green-500": "text-orange-600") + " mr-1 font-medium"
                                                }>
                                                    {orgTools?.find(o => o.toolCode === item.code && o.toolVersion === item.version) ? "已配置" : "未配置"}
                                                </span>
                                                {item.description}</p>
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
    const loadPlatformTools = async () => {
        if (!user) {
            return
        }
        setLoadingData(true);
        const res = await http.get<Tool[]>("tool/platform/tool/list")
        if (res.success) {
            // set to sep tools data
            const map: ToolSepDataMap = {
                "llm": {
                    "model": [],
                    "function": [],
                    "tool": [],
                    "other": []
                },
                "train": {
                    "dataset": [],
                    "hyperParams": [],
                    "upload": [],
                    "benchmark": [],
                    "post": [],
                    "tokenizer": [],
                    "finetune": [],
                    "other": []
                },
            }
            res.data?.forEach((t) => {
                if (t.type.startsWith("llm-")) {
                    if (map["llm"][t.type.replace("llm-", "")]) {
                        map["llm"][t.type.replace("llm-", "")].push(t)
                    } else {
                        map["llm"]["other"].push(t)
                    }
                }
                if (t.type.startsWith("train-")) {
                    if (map["train"][t.type.replace("train-", "")]) {
                        map["train"][t.type.replace("train-", "")].push(t)
                    } else {
                        map["train"]["other"].push(t)
                    }
                }
            })
            setToolDataMap(map)
        } else {
            message.error("加载平台工具出错，请刷新页面重试")
        }
        setLoadingData(false);
    }

    const loadOrgTools = async () => {
        if (!user) {
            return
        }
        const res = await http.get<OrgTool[]>("tool/org/tool/list?userId=" + user.id)
        if (res.success) {
            setOrgTools(res.data)
        } else {
            message.error("加载组织工具出错，请刷新页面重试")
        }
    }

    useEffect(() => {
        loadOrgTools().then(() => {
            loadPlatformTools()
        })
        setCurrentLocation(location.pathname.replace("/dashboard/tool/", ""))
    }, [location, user?.id]);

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
                    navigate("/dashboard/tool/" + key)
                }}
            />
            <div className="w-full grow">
                {getChildren()}
            </div>
            <Spin fullscreen spinning={loadingData} />
            <ToolDetailDrawer
                open={toolDetailDrawerOpen}
                current={currentToolDetail}
                orgTools={orgTools}
                onClose={() => {
                    setToolDetailDrawerOpen(false)
                }}
                onRefresh={() => {
                    // need refresh
                    loadOrgTools().then(() => {
                        loadPlatformTools()
                    })
                }}
            />
        </div>
    )
}