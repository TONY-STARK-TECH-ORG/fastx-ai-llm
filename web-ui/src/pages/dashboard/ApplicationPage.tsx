import {List, Button, Divider, message, Avatar, Popconfirm, Spin} from 'antd';
import {AppstoreAddOutlined, SaveOutlined, FileTextOutlined, SendOutlined, DeleteOutlined} from "@ant-design/icons";
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react'
import {
    ChevronDownIcon,
    PencilIcon,
    TrashIcon,
} from '@heroicons/react/16/solid'
import CreateApplicationModal from "../../components/dashboard/CreateApplicationModal.tsx";
import {useEffect, useState} from "react";
import {http} from "../../api/Http.ts";
import {Application, ApplicationVersion} from "../../store/define.ts";
import PageIllustrationDashboard from "../../components/page-illustration-dashboard.tsx";
import EditApplicationModal from "../../components/dashboard/EditApplicationModal.tsx";
import CreateApplicationVersionModal from "../../components/dashboard/CreateApplicationVersionModal.tsx";
import {IoCreate} from "react-icons/io5";
import {useOrganizationStore} from "../../store/OrganizationStore.ts";

export default function ApplicationPage() {
    const [createApplicationModalOpen, setCreateApplicationModalOpen] = useState(false);
    const [updateApplicationModalOpen, setUpdateApplicationModalOpen] = useState(false);
    const [createApplicationVersionModalOpen, setCreateApplicationVersionModalOpen] = useState(false);

    const [listLoading, setListLoading] = useState(false)
    const [applicationList, setApplicationList] = useState<Application[] | undefined>([])
    const [selectedApplication, setSelectedApplication] = useState<Application | undefined>(undefined)
    const [selectedApplicationVersion, setSelectedApplicationVersion] = useState<ApplicationVersion | undefined>(undefined)
    const [applicationDetailLoading, setApplicationDetailLoading] = useState(false)
    const [deleteVersion, setDeleteVersion] = useState(false)

    const [orgId] = useOrganizationStore(state => [state.id])

    const loadApplicationList = async () => {
        if (orgId === undefined) {
            return ;
        }
        const res = await http.get<Application[]>("app/list?orgId=" + orgId);
        setListLoading(true)
        if (res.success) {
            setApplicationList(res.data);
            if (selectedApplication) {
                // 重置
                const s = res.data?.find((a) => a.id === selectedApplication.id);
                setSelectedApplication(s);
                if (s?.applicationVersions && s?.applicationVersions.length !== 0) {
                    if (selectedApplicationVersion) {
                        setSelectedApplicationVersion(
                            s.applicationVersions.find((v) => v.id === selectedApplicationVersion.id)
                        )
                    } else {
                        setSelectedApplicationVersion(
                            // default set to 0
                            s?.applicationVersions[0]
                        )
                    }
                } else {
                    // no version data
                    setSelectedApplicationVersion(undefined)
                }
            }
        } else {
            message.error("加载应用列表出错，请刷新页面重试 ")
        }
        setListLoading(false)
    }

    const deleteApplication = async () => {
        if (!selectedApplication) {
            message.error("请选择要删除的应用")
            return
        }
        setApplicationDetailLoading(true)
        const res = await http.post("app/delete", {id: selectedApplication.id});
        if (res.success) {
            // set selected app to undefined
            setSelectedApplication(undefined)
            setSelectedApplicationVersion(undefined)
            // refresh
            loadApplicationList()
        } else {
            message.error("删除应用出错，请重试")
        }
        setApplicationDetailLoading(false)
    }

    const saveApplicationVersion = async () => {
        if (!selectedApplicationVersion) {
            message.error("请切换到要保存的版本")
            return
        }
        setApplicationDetailLoading(true)
        const res =
            // @TODO (stark) save data to version config.
            await http.post("app/version/update", {id: selectedApplicationVersion.id, config: JSON.stringify({
                    version: selectedApplicationVersion.version
                })})
        if (res.success) {
            message.success("版本保存成功")
            // refresh current applicationList and show loading
            loadApplicationList()
        } else {
            message.error("版本保存失败，请重试")
        }
        setApplicationDetailLoading(false)
    }

    const saveAndActiveApplicationVersion = async () => {
        if (!selectedApplicationVersion) {
            message.error("请切换到要保存发布的版本")
            return
        }
        // save version
        await saveApplicationVersion();
        // activate version
        setApplicationDetailLoading(true)
        const res =
            // @TODO (stark) save data to version config.
            await http.post("app/version/active", {id: selectedApplicationVersion.id})
        if (res.success) {
            message.success("版本发布成功")
            // refresh current applicationList and show loading
            loadApplicationList()
        } else {
            message.error("版本发布失败，请重试")
        }
        setApplicationDetailLoading(false)
    }

    const deleteApplicationVersion = async () => {
        if (!selectedApplicationVersion) {
            message.error("请切换到要删除的版本")
            return
        }
        // activate version
        setApplicationDetailLoading(true)
        const res = await http.post("app/version/delete", {id: selectedApplicationVersion.id})
        if (res.success) {
            message.success("版本删除成功")
            // refresh current applicationList and show loading
            setSelectedApplicationVersion(undefined)
        } else {
            message.error("版本删除失败，请重试")
        }
        setApplicationDetailLoading(false)
    }

    useEffect(() => {
        loadApplicationList();
    }, [orgId, deleteVersion])

    return (
        <div className="flex items-start justify-start h-screen p-2">
            <div className="w-[200px] h-full flex flex-col">
                <div className="w-full pb-2">
                    <Button className="h-[32px] w-full" type="primary" icon={<AppstoreAddOutlined />} onClick={() => {
                        setCreateApplicationModalOpen(true);
                    }}>新建应用</Button>
                </div>
                <div className="w-full bg-white grow overflow-auto rounded-[2px]">
                    <List
                        loading={listLoading}
                        className="h-full"
                        itemLayout="vertical"
                        size="small"
                        pagination={false}
                        dataSource={applicationList}
                        renderItem={(item) => (
                            <List.Item
                                key={item.id}
                                className="!p-2 hover:cursor-pointer hover:bg-[#FF6A00]/5"
                                onClick={() => {
                                    // set selected application
                                    setSelectedApplication(item)
                                    setSelectedApplicationVersion(undefined)
                                }}
                            >
                                <div className="flex w-full items-center">
                                    <Avatar className="border border-gray-200" size={40} src={item.iconUrl} shape="square" />
                                    <div className="flex flex-col ml-2 grow items-start">
                                        <p className={"text-sm font-medium"}>{item.name}</p>
                                        <div className="flex">
                                            <div
                                                className={"flex items-center justify-center rounded-[2px] px-1 py-0 " + (item.status === 'active' ? "bg-green-600" : "bg-orange-500")}>
                                                {item.status === 'active' ?
                                                    <p className="text-[10px] text-white">可访问</p> :
                                                    <p className="text-[10px] text-white">停用</p>}
                                            </div>
                                            <div
                                                className={"ml-1 flex items-center justify-center rounded-[2px] px-1 py-0 " + (item.activeVersion ? "bg-green-600" : "bg-orange-500")}>
                                                {item.activeVersion ?
                                                    <p className="text-[10px] text-white">{item.activeVersion.version}</p> :
                                                    <p className="text-[10px] text-white">未发布</p>}
                                            </div>
                                            <div
                                                className={"flex items-center justify-center rounded-[2px] px-1 py-0 bg-white border border-gray-200 ml-1"}>
                                                <p className="text-[10px] text-black">{item.type === 'agent' ? '智能体' : '体验场'}</p>
                                            </div>
                                        </div>
                                    </div>
                                    {selectedApplication && item.id === selectedApplication.id ? (
                                        <div className="w-[1px] h-[40px] bg-[#FF6A00]"></div>
                                    ) : null}
                                </div>
                            </List.Item>
                        )}
                    />
                </div>
            </div>
            {selectedApplication ? (
                <div className="grow h-full bg-white rounded-[2px] ml-2 flex flex-col items-center p-2">
                    <div className="w-full h-[30px] flex flex-row-reverse items-center">
                        <Menu>
                            <MenuButton className="inline-flex items-center gap-2 rounded-[2px] bg-[#FF6A00] px-2 text-sm/6 font-normal text-white shadow-inner shadow-white/10 focus:outline-none data-[hover]:bg-[#FF6A00]/80 data-[open]:bg-[#FF6A00] data-[focus]:outline-1 data-[focus]:outline-[#FF6A00]">
                                应用管理
                                <ChevronDownIcon className="size-4 fill-white" />
                            </MenuButton>

                            <MenuItems
                                transition
                                anchor="bottom end"
                                className="mt-2 w-52 origin-top-right rounded-[2px] border border-[#000]/5 bg-[#fff] p-1 text-sm/6 text-[#000] transition duration-100 ease-out [--anchor-gap:var(--spacing-1)] focus:outline-none data-[closed]:scale-95 data-[closed]:opacity-0"
                            >
                                <MenuItem>
                                    <button
                                        onClick={() => setUpdateApplicationModalOpen(true)}
                                        className="group flex w-full items-center gap-2 rounded-[2px] py-1.5 px-3 data-[focus]:bg-[#000]/5">
                                        <PencilIcon className="size-4 fill-[#000]" />
                                        应用编辑
                                        <kbd className="ml-auto font-sans text-xs text-[#FF6A00]">Edit</kbd>
                                    </button>
                                </MenuItem>
                                <MenuItem>
                                    <button
                                        onClick={deleteApplication}
                                        className="group flex w-full items-center gap-2 rounded-[2px] py-1.5 px-3 data-[focus]:bg-[#000]/5">
                                        <TrashIcon className="size-4 fill-[#000]"/>
                                        删除应用
                                        <kbd className="ml-auto font-sans text-xs text-[#FF6A00]">Delete</kbd>
                                    </button>
                                </MenuItem>
                            </MenuItems>
                        </Menu>
                        <Divider type="vertical"/>
                        {selectedApplication.applicationVersions && selectedApplication.applicationVersions.length !== 0 ? (
                            <div className="flex items-center">
                                <Menu>
                                    <MenuButton
                                        className="h-[24px] inline-flex items-center rounded-[2px] border border-[#FF6A00] bg-[#fff] px-2 text-sm/6 font-normal text-[#FF6A00] focus:outline-none data-[hover]:bg-[#FF6A00] data-[hover]:text-[#fff] data-[open]:bg-[#FF6A00] data-[open]:text-[#fff] fill-[#FF6A00] data-[hover]:fill-[#fff] data-[open]:fill-[#fff]  data-[focus]:outline-1 data-[focus]:outline-[#000]/10">
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
                                                onClick={() => {setCreateApplicationVersionModalOpen(true)}}
                                                className="group flex w-full items-center gap-2 rounded-[2px] py-1.5 px-3 data-[focus]:bg-[#000]/5">
                                                <IoCreate className="size-4 fill-[#000]"/>
                                                新建版本
                                            </button>
                                        </MenuItem>
                                        {
                                            selectedApplication.applicationVersions.map((item, index) => (
                                                <MenuItem key={index}>
                                                    <button
                                                        onClick={() => {
                                                            setSelectedApplicationVersion(item)
                                                            message.success("版本切换为：" + item.version)
                                                        }}
                                                        className="group flex w-full items-center gap-2 rounded-[2px] py-1.5 px-3 data-[focus]:bg-[#000]/5">
                                                        <FileTextOutlined />
                                                        {item.version}
                                                        {item.status === 'active' ? (
                                                            <kbd className="ml-auto font-sans text-xs text-green-500">在线</kbd>
                                                        ) : null}
                                                    </button>
                                                </MenuItem>
                                            ))
                                        }
                                    </MenuItems>
                                </Menu>
                                <Divider type="vertical"/>
                                <Menu>
                                    <Button
                                        onClick={saveApplicationVersion}
                                        icon={<SaveOutlined/>}
                                        size="small"
                                        type="primary"
                                        className="bg-white text-blue-500 border border-blue-500 hover:!bg-blue-500 data-[hover]:text-white shadow-none"
                                    >
                                        保存
                                    </Button>
                                </Menu>
                                <Divider type="vertical" />
                                <Menu>
                                    <Popconfirm
                                        placement="bottom"
                                        title="发布"
                                        description="该操作会直接修改线上应用，你确认发布该版本么？"
                                        okText="是的"
                                        cancelText="取消"
                                        onConfirm={saveAndActiveApplicationVersion}
                                    >
                                        <Button
                                            icon={<SendOutlined />}
                                            size="small"
                                            type="primary"
                                            className="bg-white text-green-500 border border-green-500 hover:!bg-green-500 data-[hover]:text-white shadow-none"
                                        >
                                            保存并发布
                                        </Button>
                                    </Popconfirm>
                                </Menu>
                            </div>
                        ): (
                            <Button size="small" onClick={() => setCreateApplicationVersionModalOpen(true)}>新建版本</Button>
                        )}
                        {selectedApplicationVersion ? (
                            <div className="flex items-center">
                                <Popconfirm
                                    placement="bottom"
                                    title="删除版本"
                                    description="该操作可能会影响线上应用，你确认删除该版本么？"
                                    okText="是的"
                                    cancelText="取消"
                                    onConfirm={async () => {
                                        await deleteApplicationVersion();
                                        setDeleteVersion(!deleteVersion)
                                    }}
                                >
                                    <Button size="small" danger icon={<DeleteOutlined />} />
                                </Popconfirm>
                                <Divider type="vertical" />
                            </div>
                        ) : null}
                        <div className="grow"></div>
                        <div className="flex items-center">
                            <p className="text-lg">{selectedApplication.name}</p>
                            <Divider type="vertical"/>
                            {selectedApplicationVersion ? (
                                <div className="flex items-center">
                                    <p className="text-xs text-orange-600">当前编辑：{selectedApplicationVersion.version}</p>
                                    <Divider type="vertical"/>
                                    <p className="text-xs text-gray-400">上次更新：{selectedApplicationVersion.updateTime}</p>
                                </div>
                            ) : (
                                <div>
                                <p className="text-xs">未选择版本</p>
                                </div>
                            )}
                        </div>
                    </div>
                    {selectedApplication.applicationVersions && selectedApplication.applicationVersions.length !== 0 ? (
                        <>
                            {selectedApplicationVersion ? (
                                <div className="grow w-full bg-gray-100 mt-2 flex">
                                    版本工作区
                                </div>
                            ) : (
                                <div className="grow w-full bg-gray-100 mt-2 flex items-center justify-center">
                                    <p className="text-xs">右上角选择版本进行编辑</p>
                                </div>
                            )}
                        </>
                    ) : (
                        <div className="grow w-full bg-gray-100 mt-2 flex items-center justify-center">
                            <Button onClick={() => setCreateApplicationVersionModalOpen(true)}>新建版本</Button>
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
                                    <span className="text-blue-500">FAST</span> RAG ENGINE
                                </h1>
                            </div>
                            <div>
                                <h1 className="text-xl font-normal mt-3">
                                    选择应用 丨 开始创作
                                </h1>
                            </div>
                        </div>
                    </div>
                </div>
            )}
            <Spin spinning={applicationDetailLoading} fullscreen />
            <CreateApplicationModal open={createApplicationModalOpen} onCancel={() => {
                setCreateApplicationModalOpen(false);
            }} onSuccess={async () => {
                setCreateApplicationModalOpen(false);
                // refresh current applicationList and show loading
                loadApplicationList()
            }} />
            <EditApplicationModal
                application={selectedApplication}
                open={updateApplicationModalOpen}
                onCancel={() => {
                    setUpdateApplicationModalOpen(false)
                }}
                onSuccess={() => {
                    setUpdateApplicationModalOpen(false)
                    loadApplicationList()
                }}
            />
            <CreateApplicationVersionModal
                application={selectedApplication}
                open={createApplicationVersionModalOpen}
                onCancel={() => {
                    setCreateApplicationVersionModalOpen(false)
                }}
                onSuccess={(appVer) => {
                    setCreateApplicationVersionModalOpen(false)
                    setSelectedApplicationVersion(appVer)
                    loadApplicationList()
                }} />
        </div>
    )
}