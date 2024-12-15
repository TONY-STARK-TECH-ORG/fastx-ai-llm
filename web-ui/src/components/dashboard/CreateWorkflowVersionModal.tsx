import {Modal, Input, message} from "antd";
import {useState} from "react";
import {http} from "../../api/Http.ts";
import {Workflow, WorkflowVersion} from "../../store/define.ts";

export default function CreateWorkflowVersionModal(
    {workflow, open, onCancel, onSuccess}:
    {workflow: Workflow | undefined; open: boolean; onCancel: ()=>void; onSuccess: (appVer: WorkflowVersion | undefined)=>void} ) {

    const [submitLoading, setSubmitLoading] = useState(false)
    const [cancelButtonDisable, setCancelButtonDisable] = useState(false)
    const [workflowVersion, setWorkflowVersion] = useState("1.0.0")

    return (
        <>
            <Modal
                title="新建版本"
                open={open}
                onOk={async () => {
                    // 1.0 validated content.
                    setWorkflowVersion(workflowVersion.trim())
                    if (workflowVersion.length === 0) {
                        message.error("工作流版本号不能为空!")
                        return
                    }
                    // 2.0 submit data with http-client
                    setSubmitLoading(true)
                    setCancelButtonDisable(true)
                    const res = await http.post<WorkflowVersion>("workflow/org/workflow/version/create", {
                        workflowId: workflow?.id,
                        version: workflowVersion,
                        versionData: JSON.stringify({
                            version: workflowVersion
                        }),
                    })
                    if (res.success) {
                        message.success("版本创建成功")
                        onSuccess(res.data)
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
                        <Input required placeholder="版本号: 1.0.0" value={workflowVersion} onChange={(e) => {
                            setWorkflowVersion(e.target.value)
                        }}/>
                    </div>
                </div>
            </Modal>
        </>
    )
}