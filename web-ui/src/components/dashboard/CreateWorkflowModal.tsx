import {Modal, Input, Select, message} from "antd";
import {useState} from "react";
import {http} from "../../api/Http.ts";
import {useOrganizationStore} from "../../store/OrganizationStore.ts";

export default function CreateWorkflowModal({open, onCancel, onSuccess}: {open: boolean; onCancel: ()=>void; onSuccess: ()=>void} ) {

    const [submitLoading, setSubmitLoading] = useState(false)
    const [cancelButtonDisable, setCancelButtonDisable] = useState(false)

    const [workflowName, setWorkflowName] = useState("")

    const [orgId, orgName] = useOrganizationStore(state => [state.id, state.name])

    return (
        <>
            <Modal
                title="新建工作流"
                open={open}
                onOk={async () => {
                    // 1.0 validated content.
                    setWorkflowName(workflowName.trim())
                    if (workflowName.length === 0) {
                        message.error("工作流名称不能为空!")
                        return
                    }
                    if (!orgId) {
                        message.error("请选择工作流所属组织")
                        return
                    }
                    // 2.0 submit data with http-client
                    setSubmitLoading(true)
                    setCancelButtonDisable(true)
                    const res = await http.post("workflow/org/workflow/create", {
                        name: workflowName,
                        organizationId: orgId
                    })
                    if (res.success) {
                        message.success("工作流创建成功")
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
                        <Input required placeholder="工作流名称" value={workflowName} onChange={(e) => {
                            setWorkflowName(e.target.value)
                        }}/>
                    </div>
                    <Select
                        className="mt-2"
                        placeholder="应用所属组织"
                        options={[{
                            value: orgId,
                            label: orgName
                        }]}
                        disabled
                        defaultValue={orgId}
                    />
                </div>
            </Modal>
        </>
    )
}