import {useDrag} from "./DragContext.tsx";
import {useOrganizationStore} from "../../store/OrganizationStore.ts";
import {useEffect, useState} from "react";
import {http} from "../../api/Http.ts";
import {Tool} from "../../store/tool/Tool.ts";
import {List, message} from "antd";
import {StateMapping} from "../../utils/StateMapping.ts";
import {OrgTool} from "../../store/define.ts";

type ToolSepDataMap = {
    [key: string]: Tool[]
}

export default function ToolPanel() {

    const [orgId] = useOrganizationStore(state => [state.id])
    const [loading, setLoading] = useState(false)
    const [orgTools, setOrgTools] = useState<OrgTool[]>([])
    const [toolDataMap, setToolDataMap] = useState<ToolSepDataMap>({
    "llm-model": [],
    "llm-function": [],
    "llm-tool": [],
    "train-dataset": [],
    "train-post": [],
    "train-upload": [],
    "train-tokenizer": [],
    "train-finetune": [],
    "train-hyperParams": [],
    "train-benchmark": [],
        "other": [],
    })
    const { setType, setTool, setOrgTool } = useDrag()

    const onDragStart = (event: React.DragEvent<HTMLDivElement>, nodeType: string, tool: Tool, orgTool: OrgTool) => {
        setType(nodeType);
        setTool(tool);
        setOrgTool(orgTool);
        event.dataTransfer.effectAllowed = 'move';
    };

    // load tools from platform;
    const loadTools = async () => {
        setLoading(true);
        const res = await http.get<Tool[]>("tool/platform/tool/list")
        if (res.success) {
            // set to sep tools data
            const map: ToolSepDataMap = {
                "llm-model": [],
                "llm-function": [],
                "llm-tool": [],
                "train-dataset": [],
                "train-post": [],
                "train-upload": [],
                "train-tokenizer": [],
                "train-finetune": [],
                "train-hyperParams": [],
                "train-benchmark": [],
                "other": []
            }
            res.data?.forEach((t) => {
                if (map[t.type]) {
                    map[t.type].push(t)
                } else {
                    map["other"].push(t)
                }
            })
            setToolDataMap(map)
        } else {
            message.error("加载平台工具出错，请刷新页面重试: " + res.msg)
        }
        setLoading(false);
    }

    const loadOrgTools = async () => {
        if (!orgId) {
            return
        }
        const res = await http.get<OrgTool[]>("tool/org/tool/list?orgId=" + orgId)
        if (res.success) {
            if (res.data) {
                res.data.forEach(r => r.key = r.id!!)
            }
            setOrgTools(res.data ?? [])
        } else {
            message.error("加载组织工具出错，请刷新页面重试: " + res.msg)
        }
    }

    useEffect(() => {
        orgId && loadTools();
        orgId && loadOrgTools();
    }, [orgId]);

    return (
        <div className="w-full h-full overflow-auto">
            <List
                size="small"
                loading={loading}
                dataSource={Object.keys(toolDataMap)}
                renderItem={(item) => (
                    <List.Item key={item} className="!border-none !p-1">
                        <div className="w-full flex flex-col items-start">
                            <p className="text-xs font-medium">{StateMapping.getToolName(item)}</p>
                            <div className="w-full">
                                <List
                                    size="small"
                                    dataSource={toolDataMap[item]}
                                    renderItem={(tool) => (
                                        <List.Item key={tool.code} className="!border-none !p-1">
                                            <div
                                                className="flex flex-row items-start justify-center w-full bg-gray-50 rounded py-1 px-2 dndnode hover:bg-red-50"
                                                onDragStart={(event) => {
                                                    const isHave = orgTools.find(t => t.toolCode === tool.code && t.toolVersion === tool.version);
                                                    if (isHave) {
                                                        onDragStart(event, tool.type, tool, isHave)
                                                    } else {
                                                        message.error("该工具未在当前组织中配置，请先在组织工具中配置后再试")
                                                    }
                                                }}
                                                draggable
                                            >
                                                <img className="w-5 h-5 border border-gray-300 rounded" src={tool.icon} />
                                                <p className="text-xs ml-1">{tool.name}</p>
                                                <div className="grow"></div>
                                                <div className="h-full">
                                                    {orgTools.find(t => t.toolCode === tool.code && t.toolVersion === tool.version) ? (
                                                        <p className="mt-0.5 text-[10px] text-green-500 font-medium">已配置</p>
                                                    ) : null}
                                                </div>
                                            </div>
                                        </List.Item>
                                    )}
                                />
                            </div>
                        </div>
                    </List.Item>
                )}
            />
        </div>
    )
}