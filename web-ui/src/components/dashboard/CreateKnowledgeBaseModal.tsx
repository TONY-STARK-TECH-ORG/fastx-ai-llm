import {Modal, Input, Select, message} from "antd";
import {useState} from "react";
import {http} from "../../api/Http.ts";
import {useOrganizationStore} from "../../store/OrganizationStore.ts";

const { TextArea } = Input;
export default function CreateKnowledgeBaseModal({open, onCancel, onSuccess}: {open: boolean; onCancel: ()=>void; onSuccess: ()=>void} ) {
    const [submitLoading, setSubmitLoading] = useState(false)
    const [cancelButtonDisable, setCancelButtonDisable] = useState(false)

    const [knowledgeBaseName, setKnowledgeBaseName] = useState("")
    const [KnowledgeBaseDesc, setKnowledgeBaseDesc] = useState("")

    const [orgId, orgName] = useOrganizationStore(state => [state.id, state.name])

    return (
        <>
            <Modal
                title="新建知识库"
                open={open}
                onOk={async () => {
                    // 1.0 validated content.
                    setKnowledgeBaseName(knowledgeBaseName.trim())
                    if (knowledgeBaseName.length === 0) {
                        message.error("知识库名称不能为空!")
                        return
                    }
                    setKnowledgeBaseDesc(KnowledgeBaseDesc.trim())
                    if (KnowledgeBaseDesc.length > 100) {
                        message.error("知识库简介不能超过100字!")
                        return
                    }
                    if (KnowledgeBaseDesc.length === 0) {
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
                    const res = await http.post("knowledge/create", {
                        name: knowledgeBaseName,
                        description: KnowledgeBaseDesc,
                        organizationId: orgId
                    })
                    if (res.success) {
                        message.success("知识库创建成功")
                        onSuccess()
                    } else {
                        message.error(res.msg)
                    }
                    setSubmitLoading(false)
                    setCancelButtonDisable(false)
                }}
                onCancel={onCancel}
                okButtonProps={{ loading: submitLoading, className: "bg-blue-600 shadow-none hover:!bg-blue-600/80" }}
                cancelButtonProps={{ disabled: cancelButtonDisable }}
                maskClosable={cancelButtonDisable}
            >
                <div className="w-full flex flex-col pb-4">
                    <div>
                        <Input
                            required
                            placeholder="知识库名称"
                            value={knowledgeBaseName}
                            onChange={(e) => {
                            setKnowledgeBaseName(e.target.value)
                        }}/>
                    </div>
                    <Select
                        className="mt-2"
                        placeholder="选择知识库所属组织"
                        options={[
                            {
                                value: orgId,
                                label: orgName
                            }
                        ]}
                        disabled
                        defaultValue={orgId}
                    />
                    <TextArea
                        className="mt-2"
                        required
                        showCount
                        rows={4}
                        placeholder="知识库简介 (100字内)"
                        maxLength={105}
                        value={KnowledgeBaseDesc}
                        onChange={(e) => {
                            setKnowledgeBaseDesc(e.target.value)
                        }}
                    />
                </div>
            </Modal>
        </>
    )
}