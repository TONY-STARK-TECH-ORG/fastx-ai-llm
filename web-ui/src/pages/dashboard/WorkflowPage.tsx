import {List, Button, Divider, message, Spin, Popconfirm} from 'antd';
import {
    AppstoreAddOutlined, DeleteOutlined, LayoutOutlined, RadiusUprightOutlined, SaveOutlined, ZoomInOutlined
} from "@ant-design/icons";
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react'
import {
    ChevronDownIcon,
    PencilIcon,
    TrashIcon,
} from '@heroicons/react/16/solid'
import {useEffect, useState} from "react";
import {http} from "../../api/Http.ts";
import PageIllustrationDashboard from "../../components/page-illustration-dashboard.tsx";

import {useOrganizationStore} from "../../store/OrganizationStore.ts";
import {Workflow, WorkflowVersion} from "../../store/define.ts";
import CreateWorkflowModal from "../../components/dashboard/CreateWorkflowModal.tsx";
import EditWorkflowModal from "../../components/dashboard/EditWorkflowModal.tsx";
import CreateWorkflowVersionModal from "../../components/dashboard/CreateWorkflowVersionModal.tsx";
import WorkflowPanel from "../../components/workflow/WorkFlowPanel.tsx";
import {ReactFlowProvider} from "@xyflow/react";
import useWorkflowStore from "../../store/WorkflowStore.ts";
import {DragProvider} from "../../components/workflow/DragContext.tsx";

export default function WorkflowPage() {
    const [orgId] = useOrganizationStore(state => [state.id])
    const [
        onLayout,
        onZoomSelected,
        onSave,
        onInit,
    ] = useWorkflowStore(state => [state.onLayout, state.onZoomSelected, state.onSave, state.onInit])

    const [pageLoading, setPageLoading] = useState(false)

    const [createWorkflowModalOpen, setCreateWorkflowModalOpen] = useState(false);
    const [updateWorkflowModalOpen, setUpdateWorkflowModalOpen] = useState(false);

    const [createWorkflowVersionModalOpen, setCreateWorkflowVersionModalOpen] = useState(false)
    
    const [workflowList, setWorkFlowList] = useState<Workflow[] | undefined>([])
    const [selectedWorkFlow, setSelectedWorkFlow] = useState<Workflow | undefined>(undefined)
    const [selectedWorkFlowVersion, setSelectedWorkFlowVersion] = useState<WorkflowVersion | undefined>(undefined)
    const [selectedWorkFlowVersionList, setSelectedWorkFlowVersionList] = useState<WorkflowVersion[] | undefined>([])

    const deleteWorkflow = async () => {
        // delete current workflow
        if (!selectedWorkFlow) {
            return
        }
        setPageLoading(true)
        const res = await http.post("workflow/org/workflow/delete", {
            id: selectedWorkFlow.id
        })
        setPageLoading(false)

        if (res.success) {
            // clean state
            setSelectedWorkFlow(undefined)
            // reload list
            message.success("工作流删除成功")
            loadWorkflowList();
        } else {
            message.error("工作流删除失败，请重试: " + res.msg)
        }
    }

    const loadWorkflowList = async () => {
        if (!orgId) {
            return
        }
        setPageLoading(true)
        const res = await http.getWithParams<Workflow[]>("workflow/org/workflow/list", {
            orgId: orgId
        })
        setPageLoading(false)
        if (res.success) {
            setWorkFlowList(res.data)
            if (selectedWorkFlow) {
                setSelectedWorkFlow(res?.data?.find(w => w.id === selectedWorkFlow.id))
                loadWorkflowVersionList(selectedWorkFlow.id!!)
            }
        } else {
            message.error("工作流加载失败，请重试: " + res.msg)
        }
    }

    const loadWorkflowVersionList = async (workflowId: string) => {
        setPageLoading(true)
        const res = await http.getWithParams<WorkflowVersion[]>("workflow/org/workflow/version/list", {
            workflowId: workflowId
        })
        if (res.success) {
            setSelectedWorkFlowVersionList(res.data)
            if (selectedWorkFlowVersion) {
                setSelectedWorkFlowVersion(res?.data?.find(w => w.id === selectedWorkFlowVersion.id))
            }
        } else {
            message.error("工作流版本加载失败，请重试: " + res.msg)
        }
        setPageLoading(false)
    }

    const updateWorkflowVersion = async (status: string | undefined, versionData: string | undefined) => {
        if (!selectedWorkFlowVersion) {
            return
        }
        setPageLoading(true)
        const res = await http.post("workflow/org/workflow/version/update", {
            id: selectedWorkFlowVersion.id,
            workflowId: selectedWorkFlowVersion.workflowId,
            status: status ? status : selectedWorkFlowVersion.status,
            versionData: versionData ? versionData : selectedWorkFlowVersion.versionData,
            version: selectedWorkFlowVersion.version
        })
        setPageLoading(false)
        if (res.success) {
            message.success("工作流版本更新/保存成功")
            await loadWorkflowList();
            await loadWorkflowVersionList(selectedWorkFlowVersion.workflowId)
        } else {
            message.error("工作流版本更新/保存失败，请重试: " + res.msg)
        }
    }

    const activeWorkflowVersion = async () => {
        if (!selectedWorkFlowVersion) {
            return
        }
        await updateWorkflowVersion("active", undefined);
    }

    const deleteWorkflowVersion = async () => {
        if (!selectedWorkFlowVersion) {
            return
        }
        setPageLoading(true)
        const res = await http.post("workflow/org/workflow/version/delete", {
            id: selectedWorkFlowVersion.id
        })
        setPageLoading(false)
        if (res.success) {
            message.success("工作流版本删除成功")
            await loadWorkflowVersionList(selectedWorkFlowVersion.workflowId)
        } else {
            message.error("工作流版本删除失败，请重试: " + res.msg)
        }
    }

    useEffect(() => {
        if (selectedWorkFlowVersion &&
            selectedWorkFlowVersion.versionData &&
            selectedWorkFlowVersion.versionData.length !== 0) {
            onInit(selectedWorkFlowVersion.versionData)
        }
    }, [selectedWorkFlowVersion]);

    useEffect(() => {
        loadWorkflowList()
    }, [orgId])

    return (
        <div className="flex items-start justify-start h-screen p-2">
            <div className="w-[200px] min-w-[200px] h-full flex flex-col">
                <div className="w-full pb-2">
                    <Button className="h-[32px] w-full bg-red-600 shadow-none hover:!bg-red-600/80" type="primary" icon={<AppstoreAddOutlined />} onClick={() => {
                        setCreateWorkflowModalOpen(true);
                    }}>新建工作流</Button>
                </div>
                <div className="w-full bg-white grow overflow-auto rounded-[2px]">
                    <List
                        className="h-full"
                        itemLayout="vertical"
                        size="small"
                        pagination={false}
                        dataSource={workflowList}
                        renderItem={(item) => (
                            <List.Item
                                key={item.id}
                                className="!p-2 hover:cursor-pointer hover:bg-red-600/5"
                                onClick={async () => {
                                    await setSelectedWorkFlow(item)
                                    await loadWorkflowVersionList(item.id!!)
                                }}
                            >
                                <div className="flex w-full items-center">
                                    <div className="flex flex-col ml-2 grow items-start">
                                        <p className={"text-sm font-medium"}>{item.name}</p>
                                        <div className="flex mt-2 items-center">
                                            <div
                                                className={"flex items-center justify-center rounded-[2px] px-1 py-0 " + (item.status === 'active' ? "bg-green-600" : "bg-orange-500")}>
                                                {item.status === 'active' ?
                                                    <p className="text-[10px] text-white">启用</p> :
                                                    <p className="text-[10px] text-white">停用</p>}
                                            </div>
                                            <div
                                                className={"ml-0.5 flex items-center justify-center rounded-[2px] px-1 py-0 " + (item.activeVersion ? "bg-green-600" : "bg-orange-500")}>
                                                {item.activeVersion ?
                                                    <p className="text-[10px] text-white">版本在线</p> :
                                                    <p className="text-[10px] text-white">草稿</p>}
                                            </div>
                                        </div>
                                    </div>
                                    {selectedWorkFlow && item.id === selectedWorkFlow.id ? (
                                        <div className="w-[1px] h-[60px] bg-red-600"></div>
                                    ) : null}
                                </div>
                            </List.Item>
                        )}
                    />
                </div>
            </div>
            {selectedWorkFlow ? (
                <div className="grow h-full bg-white rounded-[2px] ml-2 flex flex-col items-center p-2">
                    <div className="w-full h-[30px] flex flex-row-reverse items-center">
                        <Menu>
                            <MenuButton className="inline-flex items-center gap-2 rounded-[2px] bg-red-600 px-2 text-sm/6 font-normal text-white shadow-inner shadow-white/10 focus:outline-none data-[hover]:bg-red-600/80 data-[open]:bg-red-600 data-[focus]:outline-1 data-[focus]:outline-red-600">
                                设置
                                <ChevronDownIcon className="size-4 fill-white" />
                            </MenuButton>

                            <MenuItems
                                transition
                                anchor="bottom end"
                                className="mt-2 w-52 origin-top-right rounded-[2px] border border-[#000]/5 bg-[#fff] p-1 text-sm/6 text-[#000] transition duration-100 ease-out [--anchor-gap:var(--spacing-1)] focus:outline-none data-[closed]:scale-95 data-[closed]:opacity-0"
                            >
                                <MenuItem>
                                    <button
                                        onClick={() => setUpdateWorkflowModalOpen(true)}
                                        className="group flex w-full items-center gap-2 rounded-[2px] py-1.5 px-3 data-[focus]:bg-[#000]/5">
                                        <PencilIcon className="size-4 fill-[#000]" />
                                        基础信息编辑
                                        <kbd className="ml-auto font-sans text-xs text-red-600">Edit</kbd>
                                    </button>
                                </MenuItem>
                                <MenuItem>
                                    <button
                                        onClick={deleteWorkflow}
                                        className="group flex w-full items-center gap-2 rounded-[2px] py-1.5 px-3 data-[focus]:bg-[#000]/5">
                                        <TrashIcon className="size-4 fill-[#000]"/>
                                        删除工作流
                                        <kbd className="ml-auto font-sans text-xs text-red-600">Delete</kbd>
                                    </button>
                                </MenuItem>
                            </MenuItems>
                        </Menu>
                        <Divider type="vertical" />
                        <Menu>
                            <MenuButton className="h-[24px] inline-flex items-center rounded-[2px] border border-red-500 bg-[#fff] px-2 text-sm/6 font-normal text-red-500 focus:outline-none data-[hover]:bg-red-500 data-[hover]:text-[#fff] data-[open]:bg-red-500 data-[open]:text-[#fff] fill-red-500 data-[hover]:fill-[#fff] data-[open]:fill-[#fff]  data-[focus]:outline-1 data-[focus]:outline-[#000]/10">
                                版本
                                <ChevronDownIcon className="size-4 fill-inherit" />
                            </MenuButton>

                            <MenuItems
                                transition
                                anchor="bottom end"
                                className="mt-2 w-52 origin-top-right rounded-[2px] border border-[#000]/5 bg-[#fff] p-1 text-sm/6 text-[#000] transition duration-100 ease-out [--anchor-gap:var(--spacing-1)] focus:outline-none data-[closed]:scale-95 data-[closed]:opacity-0"
                            >
                                <MenuItem>
                                    <button
                                        onClick={() => setCreateWorkflowVersionModalOpen(true)}
                                        className="group flex w-full items-center gap-2 rounded-[2px] py-1.5 px-3 data-[focus]:bg-[#000]/5">
                                        <PencilIcon className="size-4 fill-[#000]" />
                                        新建版本
                                    </button>
                                </MenuItem>
                                {selectedWorkFlowVersionList?.map(item => (
                                    <MenuItem key={item.id}>
                                        <button
                                            onClick={() => setSelectedWorkFlowVersion(item)}
                                            className="group flex w-full items-center gap-2 rounded-[2px] py-1.5 px-3 data-[focus]:bg-[#000]/5">
                                            <RadiusUprightOutlined className={selectedWorkFlowVersion?.id === item.id ? "text-red-500" : ""} />
                                            <span className={selectedWorkFlowVersion?.id === item.id ? "text-red-500" : ""}>{item.version}</span>
                                            <kbd className="ml-auto font-sans text-xs text-green-600">{item.status === 'active' ? "线上版本" : ""}</kbd>
                                        </button>
                                    </MenuItem>
                                ))}

                            </MenuItems>
                        </Menu>
                        {selectedWorkFlowVersion ? (
                            <>
                                <Divider type="vertical"/>
                                <div>
                                    <Button
                                        type="primary"
                                        size="small"
                                        onClick={activeWorkflowVersion}
                                        icon={<RadiusUprightOutlined/>}
                                    >
                                        发布
                                    </Button>
                                </div>
                                <Divider type="vertical"/>
                                <Button
                                    className="hover:!border-green-500"
                                    size="small"
                                    onClick={async () => {
                                        const data = onSave();
                                        await updateWorkflowVersion(undefined, JSON.stringify(data));
                                    }}
                                    icon={<SaveOutlined className="text-lime-600" />}
                                />
                                <Button
                                    className="mr-2"
                                    size="small"
                                    onClick={() => {
                                        onLayout()
                                    }}
                                    icon={<LayoutOutlined/>}
                                />
                                <Button
                                    className="mr-2"
                                    size="small"
                                    onClick={() => {
                                        onZoomSelected()
                                    }}
                                    icon={<ZoomInOutlined/>}
                                />
                                <Divider type="vertical"/>
                                <div>
                                    <Popconfirm
                                        placement="bottom"
                                        title="删除版本"
                                        description="该操作可能会直接影响线上应用，你确认删除该版本么？"
                                        okText="是的"
                                        cancelText="取消"
                                        onConfirm={deleteWorkflowVersion}
                                    >
                                        <Button
                                            size="small"
                                            danger
                                            icon={<DeleteOutlined/>}
                                        />
                                    </Popconfirm>
                                </div>
                            </>
                        ) : null}
                        <div className="grow"></div>
                        <div className="flex items-center">
                            <p className="text-lg">{selectedWorkFlow.name}</p>
                            {selectedWorkFlowVersion ? (
                                <div className="flex items-center">
                                    <Divider type="vertical"/>
                                    <div>
                                        <p className="text-xs text-red-600">正在编辑：{selectedWorkFlowVersion.version}</p>
                                    </div>
                                </div>
                            ) : null}
                        </div>
                    </div>
                    {selectedWorkFlowVersion ? (
                        <div
                            className="grow w-full bg-gray-100/50 mt-2 flex flex-col items-start justify-start p-2 overflow-auto">
                            <ReactFlowProvider>
                                <DragProvider>
                                    <WorkflowPanel />
                                </DragProvider>
                            </ReactFlowProvider>
                        </div>
                    ) : (
                        <div className="grow w-full bg-gray-100/50 mt-2 flex flex-col items-center justify-center p-2 overflow-auto">
                            <h1 className="text-xl font-normal mt-3">
                                右上角新建版本丨开始创作
                            </h1>
                        </div>
                    )}
                </div>
            ) : (
                <div className="grow h-full bg-white rounded-[2px] ml-2 flex flex-col items-center justify-center">
                    <PageIllustrationDashboard />
                    <div className="mx-auto max-w-6xl px-4 sm:px-6">
                        {/* Hero content */}
                        <div className="flex flex-col items-center justify-center">
                            {/* Section header */}
                            <div className="text-center">
                                <h1
                                    className="border-y text-5xl font-bold [border-image:linear-gradient(to_right,transparent,theme(colors.slate.300/.8),transparent)1] md:text-6xl"
                                >
                                    <span className="text-lime-600">FLOW</span><span className="text-cyan-600 ml-6">EVERYTHING</span>
                                </h1>
                            </div>
                            <div>
                                <h1 className="text-xl font-normal mt-3">
                                    工作流是所有应用、任务的基础
                                </h1>
                            </div>
                        </div>
                    </div>
                </div>
            )}
            <Spin spinning={pageLoading} fullscreen />
            <CreateWorkflowModal open={createWorkflowModalOpen} onCancel={() => setCreateWorkflowModalOpen(false)} onSuccess={() => {
                setCreateWorkflowModalOpen(false);
                loadWorkflowList();
            }} />
            <EditWorkflowModal workflow={selectedWorkFlow} open={updateWorkflowModalOpen} onCancel={() => setUpdateWorkflowModalOpen(false)} onSuccess={() => {
                setUpdateWorkflowModalOpen(false);
                loadWorkflowList();
            }}/>
            <CreateWorkflowVersionModal workflow={selectedWorkFlow} open={createWorkflowVersionModalOpen} onCancel={() => setCreateWorkflowVersionModalOpen(false)} onSuccess={(createdVersion) => {
                setCreateWorkflowVersionModalOpen(false);
                loadWorkflowVersionList(createdVersion?.workflowId!!)
            }} />
        </div>
    )
}