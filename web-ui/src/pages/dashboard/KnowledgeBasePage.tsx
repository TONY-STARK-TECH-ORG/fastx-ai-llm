import {List, Button, Divider, message, Spin, Card, Upload} from 'antd';
import {
    AppstoreAddOutlined, CloseSquareOutlined,
    CloudServerOutlined, CoffeeOutlined,
    FolderViewOutlined,
} from "@ant-design/icons";
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react'
import {
    ChevronDownIcon,
    PencilIcon,
    TrashIcon,
} from '@heroicons/react/16/solid'
import {useContext, useEffect, useState} from "react";
import {api, http} from "../../api/Http.ts";
import {UserContext} from "../../context/UserContext.ts";
import {KnowledgeBase, KnowledgeBaseFile} from "../../store/define.ts";
import PageIllustrationDashboard from "../../components/page-illustration-dashboard.tsx";
import CreateKnowledgeBaseModal from "../../components/dashboard/CreateKnowledgeBaseModal.tsx";
import EditKnowledgeBaseModal from "../../components/dashboard/EditKnowledgeBaseModal.tsx";

import type { UploadProps } from 'antd';
const { Meta } = Card;


export default function KnowledgeBasePage() {
    const [createKnowledgeBaseModalOpen, setCreateKnowledgeBaseModalOpen] = useState(false);
    const [updateKnowledgeBaseModalOpen, setUpdateKnowledgeBaseModalOpen] = useState(false);

    const [listLoading, setListLoading] = useState(false)
    const [knowledgeBaseList, setKnowledgeBaseList] = useState<KnowledgeBase[] | undefined>([])
    const [knowledgeBaseFileList, setKnowledgeBaseFileList] = useState<KnowledgeBaseFile[] | undefined>([])
    const [selectedKnowledgeBase, setSelectedKnowledgeBase] = useState<KnowledgeBase | undefined>(undefined)
    const [knowledgeBaseDetailLoading, setKnowledgeBaseDetailLoading] = useState(false)

    const { user } = useContext(UserContext)

    const loadKnowledgeBaseList = async () => {
        if (user === undefined) {
            return ;
        }
        const res = await http.get<KnowledgeBase[]>("knowledge/list?userId=" + user?.id);
        setListLoading(true)
        if (res.success) {
            setKnowledgeBaseList(res.data);
            if (selectedKnowledgeBase) {
                // 重置
                setSelectedKnowledgeBase(res.data?.find((a) => a.id === selectedKnowledgeBase.id));
            }
        } else {
            message.error("加载知识库列表出错，请刷新页面重试 ")
        }
        setListLoading(false)
    }

    const deleteKnowledgeBase = async () => {
        if (!selectedKnowledgeBase) {
            message.error("请选择要删除的知识库")
            return
        }
        setKnowledgeBaseDetailLoading(true)
        const res = await http.post("knowledge/delete", {id: selectedKnowledgeBase.id});
        if (res.success) {
            // set selected app to undefined
            setSelectedKnowledgeBase(undefined)
            // refresh
            loadKnowledgeBaseList()
        } else {
            message.error("删除知识库出错，请重试")
        }
        setKnowledgeBaseDetailLoading(false)
    }

    const setSelectedKnowledgeBaseAndLoadFile = async (item: KnowledgeBase) => {
        setSelectedKnowledgeBase(item)
        await loadKnowledgeBaseFileList(item.id)
    }

    const loadKnowledgeBaseFileList = async (id: string) => {
        if (!id) {
            message.error("请先选择知识库")
            return
        }
        setKnowledgeBaseDetailLoading(true)
        const res =
            await http.get<KnowledgeBaseFile[]>("knowledge/file/list?knowledgeId=" + id);
        if (res.success) {
            setKnowledgeBaseFileList(res.data)
        } else {
            message.error("加载知识库文档列表失败，请重试")
        }
        setKnowledgeBaseDetailLoading(false)
    }

    const handleChange: UploadProps['onChange'] = (info) => {
        const { status } = info.file;
        if (status === 'uploading') {
            return ;
        }

        const resp = info.file.response;
        const fileUploadSuccess = status === 'done' && resp.code === 200;
        message.success(fileUploadSuccess ? `${info.file.name} 文件上传成功` : `${info.file.name} 文件上传失败`);

        if (fileUploadSuccess) {
            info.file.url = resp.data[0]['downloadUrl'];
        }
        selectedKnowledgeBase && loadKnowledgeBaseFileList(selectedKnowledgeBase.id)
    }

    const deleteKnowledgeBaseFile = async (item: KnowledgeBaseFile) => {
        setKnowledgeBaseDetailLoading(true)
        const res =
            await http.post("knowledge/file/delete", [{id: item.id}]);
        if (res.success) {
            selectedKnowledgeBase && await loadKnowledgeBaseFileList(selectedKnowledgeBase?.id)
            message.success("文档删除成功")
        } else {
            message.error("删除知识库文档失败，请重试")
        }
        setKnowledgeBaseDetailLoading(false)
    }

    useEffect(() => {
        loadKnowledgeBaseList();
    }, [user?.token])

    return (
        <div className="flex items-start justify-start h-screen p-2">
            <div className="w-[200px] min-w-[200px] h-full flex flex-col">
                <div className="w-full pb-2">
                    <Button className="h-[32px] w-full bg-blue-600 shadow-none hover:!bg-blue-600/80" type="primary" icon={<AppstoreAddOutlined />} onClick={() => {
                        setCreateKnowledgeBaseModalOpen(true);
                    }}>新建知识库</Button>
                </div>
                <div className="w-full bg-white grow overflow-auto rounded-[2px]">
                    <List
                        loading={listLoading}
                        className="h-full"
                        itemLayout="vertical"
                        size="small"
                        pagination={false}
                        dataSource={knowledgeBaseList}
                        renderItem={(item) => (
                            <List.Item
                                key={item.id}
                                className="!p-2 hover:cursor-pointer hover:bg-blue-600/5"
                                onClick={async () => {
                                    // set selected application
                                    await setSelectedKnowledgeBaseAndLoadFile(item)
                                }}
                            >
                                <div className="flex w-full items-center">
                                    <div className="flex flex-col ml-2 grow items-start">
                                        <p className={"text-sm font-medium"}>{item.name}</p>
                                        <div className="flex">
                                            <div
                                                className={"flex items-center justify-center rounded-[2px] px-1 py-0 " + (item.status === 'active' ? "bg-green-600" : "bg-orange-500")}>
                                                {item.status === 'active' ?
                                                    <p className="text-[10px] text-white">可用</p> :
                                                    <p className="text-[10px] text-white">未发布</p>}
                                            </div>
                                        </div>
                                    </div>
                                    {selectedKnowledgeBase && item.id === selectedKnowledgeBase.id ? (
                                        <div className="w-[1px] h-[40px] bg-blue-600"></div>
                                    ) : null}
                                </div>
                            </List.Item>
                        )}
                    />
                </div>
            </div>
            {selectedKnowledgeBase ? (
                <div className="grow h-full bg-white rounded-[2px] ml-2 flex flex-col items-center p-2">
                    <div className="w-full h-[30px] flex flex-row-reverse items-center">
                        <Menu>
                            <MenuButton className="inline-flex items-center gap-2 rounded-[2px] bg-blue-600 px-2 text-sm/6 font-normal text-white shadow-inner shadow-white/10 focus:outline-none data-[hover]:bg-blue-600/80 data-[open]:bg-blue-600 data-[focus]:outline-1 data-[focus]:outline-blue-600">
                                知识库设置
                                <ChevronDownIcon className="size-4 fill-white" />
                            </MenuButton>

                            <MenuItems
                                transition
                                anchor="bottom end"
                                className="mt-2 w-52 origin-top-right rounded-[2px] border border-[#000]/5 bg-[#fff] p-1 text-sm/6 text-[#000] transition duration-100 ease-out [--anchor-gap:var(--spacing-1)] focus:outline-none data-[closed]:scale-95 data-[closed]:opacity-0"
                            >
                                <MenuItem>
                                    <button
                                        onClick={() => setUpdateKnowledgeBaseModalOpen(true)}
                                        className="group flex w-full items-center gap-2 rounded-[2px] py-1.5 px-3 data-[focus]:bg-[#000]/5">
                                        <PencilIcon className="size-4 fill-[#000]" />
                                        基础信息编辑
                                        <kbd className="ml-auto font-sans text-xs text-blue-600">Edit</kbd>
                                    </button>
                                </MenuItem>
                                <MenuItem>
                                    <button
                                        onClick={deleteKnowledgeBase}
                                        className="group flex w-full items-center gap-2 rounded-[2px] py-1.5 px-3 data-[focus]:bg-[#000]/5">
                                        <TrashIcon className="size-4 fill-[#000]"/>
                                        删除知识库
                                        <kbd className="ml-auto font-sans text-xs text-blue-600">Delete</kbd>
                                    </button>
                                </MenuItem>
                            </MenuItems>
                        </Menu>
                        <Divider type="vertical" />
                        <div>
                            <Upload
                                action={api("file/uploadToWorkSpace")}
                                headers={http.uploadHeaders()}
                                data={{
                                    workspaceId: selectedKnowledgeBase.id
                                }}
                                multiple={true}
                                showUploadList={false}
                                onChange={handleChange}
                            >
                                {knowledgeBaseFileList && knowledgeBaseFileList?.length >= 999 ? null : <Button className="hover:!text-blue-600 hover:!border-blue-600" size="small" type="default">添加文件</Button>}
                            </Upload>
                        </div>
                        <div className="grow"></div>
                        <div className="flex items-center">
                            <p className="text-lg">{selectedKnowledgeBase.name}</p>
                            <Divider type="vertical"/>
                            {selectedKnowledgeBase.status === 'active' ? (
                                <div>
                                    <p className="text-xs text-green-600">可用</p>
                                </div>
                            ): (
                                <div>
                                    <p className="text-xs">未发布</p>
                                </div>
                            )}
                        </div>
                    </div>
                    <div className="grow w-full bg-gray-100/50 mt-2 flex flex-col items-start justify-start p-2 overflow-auto">
                        <List
                            className="w-full h-full"
                            grid={{ gutter: 16, column: 4 }}
                            dataSource={knowledgeBaseFileList}
                            renderItem={(item) => (
                                <List.Item>
                                    <div>
                                        <Card
                                            className="hover:cursor-pointer"
                                            actions={[
                                                <FolderViewOutlined key="preview" />,
                                                <CloseSquareOutlined onClick={async () => {
                                                    await deleteKnowledgeBaseFile(item)
                                                }} key="delete" />,
                                                item.status === 'active' ? <CloudServerOutlined key='active' style={{color: "green"}} /> : <CoffeeOutlined key='wait' style={{color:"orange"}} />,
                                            ]}
                                            styles={{
                                                body:{
                                                    padding: 0,
                                                },
                                                actions: {
                                                    padding: "0px"
                                                }
                                            }}
                                        >
                                            <Meta
                                                title={
                                                    <div className="flex items-center w-full h-[64px] px-2">
                                                        {item.name.endsWith("png") ?
                                                            <img className="border border-gray-100" width={44} height={44} src={item.downloadUrl}/> :
                                                            <img className="border border-gray-100" width={44} height={44} src="https://oss.fastx-ai.com/fastx-ai-llm/123/file.jpg" />
                                                        }
                                                        <p className="text-[14px] font-normal ml-2 truncate text-wrap">{item.name}</p>
                                                    </div>
                                                }
                                            />
                                        </Card>
                                    </div>
                                </List.Item>
                            )}
                        />
                    </div>
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
                                    <span className="text-blue-500">KNOW</span><span className="text-purple-600 ml-6">LEDGE</span>
                                </h1>
                            </div>
                            <div>
                                <h1 className="text-xl font-normal mt-3">
                                    知识库可以存储大量数据用于 LLM 检索
                                </h1>
                            </div>
                        </div>
                    </div>
                </div>
            )}
            <Spin spinning={knowledgeBaseDetailLoading} fullscreen />
            <CreateKnowledgeBaseModal open={createKnowledgeBaseModalOpen} onCancel={() => {
                setCreateKnowledgeBaseModalOpen(false);
            }} onSuccess={async () => {
                setCreateKnowledgeBaseModalOpen(false);
                // refresh current applicationList and show loading
                loadKnowledgeBaseList()
            }} />
            <EditKnowledgeBaseModal
                knowledgeBase={selectedKnowledgeBase}
                open={updateKnowledgeBaseModalOpen}
                onCancel={() => {
                    setUpdateKnowledgeBaseModalOpen(false)
                }}
                onSuccess={() => {
                    setUpdateKnowledgeBaseModalOpen(false)
                    loadKnowledgeBaseList()
                }}
            />
        </div>
    )
}