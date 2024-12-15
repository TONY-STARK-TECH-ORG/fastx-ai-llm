import {Modal, Input, Select, message} from "antd";
import {useEffect, useState} from "react";
import {http} from "../../api/Http.ts";
import {KnowledgeBase} from "../../store/define.ts";
import {useOrganizationStore} from "../../store/OrganizationStore.ts";

const { TextArea } = Input;
export default function EditKnowledgeBaseModal(
    {knowledgeBase, open, onCancel, onSuccess}:
    {knowledgeBase: KnowledgeBase | undefined; open: boolean; onCancel: ()=>void; onSuccess: ()=>void} ) {

    const [selectedOption, setSelectedOption] = useState<string | undefined>("inactive")
    const [submitLoading, setSubmitLoading] = useState(false)
    const [cancelButtonDisable, setCancelButtonDisable] = useState(false)

    const [knowledgeBaseName, setKnowledgeBaseName] = useState<string | undefined>(undefined)
    const [knowledgeBaseDesc, setKnowledgeBaseDesc] = useState<string | undefined>(undefined)

    const [orgId, orgName] = useOrganizationStore(state => [state.id, state.name])

    useEffect(() => {
        if (open) {
            setSelectedOption(knowledgeBase?.status)
            setKnowledgeBaseDesc(knowledgeBase?.description)
            setKnowledgeBaseName(knowledgeBase?.name)
        }

        return () => {
            setKnowledgeBaseName(undefined)
            setKnowledgeBaseDesc(undefined)
            setSelectedOption("agent")
        }
    }, [open])

    return (
        <>
            <Modal
                title="编辑知识库"
                open={open}
                onOk={async () => {
                    // 1.0 validated content.
                    setKnowledgeBaseName(knowledgeBaseName?.trim())
                    if (knowledgeBaseName?.length === 0) {
                        message.error("知识库名称不能为空!")
                        return
                    }
                    setKnowledgeBaseDesc(knowledgeBaseDesc?.trim())
                    if (knowledgeBaseDesc && knowledgeBaseDesc.length > 100) {
                        message.error("知识库简介不能超过100字!")
                        return
                    }
                    if (knowledgeBaseDesc?.length === 0) {
                        message.error("请输入简短的知识库描述")
                        return
                    }
                    if (!orgId) {
                        message.error("请选择知识库所属组织")
                        return
                    }
                    // 2.0 submit data with http-client
                    setSubmitLoading(true)
                    setCancelButtonDisable(true)
                    const res = await http.post("knowledge/update", {
                        id: knowledgeBase?.id,
                        name: knowledgeBaseName,
                        description: knowledgeBaseDesc,
                        status: selectedOption,
                        iconUrl: "https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png",
                        organizationId: orgId
                    })
                    if (res.success) {
                        message.success("知识库更新成功")
                        onSuccess()
                    } else {
                        message.error(res.msg)
                    }
                    setSubmitLoading(false)
                    setCancelButtonDisable(false)
                }}
                onCancel={onCancel}
                okButtonProps={{ loading: submitLoading }}
                cancelButtonProps={{ disabled: cancelButtonDisable }}
                maskClosable={cancelButtonDisable}
            >
                <div className="w-full flex flex-col pb-4">
                    <div>
                        <Input required placeholder="知识库名称" value={knowledgeBaseName} onChange={(e) => {
                            setKnowledgeBaseName(e.target.value)
                        }}/>
                        <Select
                            className="mt-2 w-full"
                            defaultValue={selectedOption}
                            onChange={(value) => {
                                setSelectedOption(value)
                            }}
                            options={[
                                {value: 'active', label: '在线'},
                                {value: 'inactive', label: '离线'},
                            ]}
                        />
                    </div>
                    <Select
                        defaultValue={orgId}
                        className="mt-2"
                        placeholder="选择知识库所属组织"
                        options={[
                            {
                                value: orgId,
                                label: orgName
                            }
                        ]}
                        disabled
                    />
                    <TextArea required showCount className="mt-2" rows={4} placeholder="知识库简介 (100字内)" maxLength={105} value={knowledgeBaseDesc} onChange={(e) => {
                        setKnowledgeBaseDesc(e.target.value)
                    }}/>
                </div>
            </Modal>
        </>
    )
}