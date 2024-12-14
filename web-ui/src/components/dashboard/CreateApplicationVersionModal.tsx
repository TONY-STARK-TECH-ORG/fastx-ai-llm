import {Modal, Input, message} from "antd";
import {useState} from "react";
import {http} from "../../api/Http.ts";
import {Application, ApplicationVersion} from "../../store/define.ts";

export default function CreateApplicationVersionModal(
    {application, open, onCancel, onSuccess}:
    {application: Application | undefined; open: boolean; onCancel: ()=>void; onSuccess: (appVer: ApplicationVersion | undefined)=>void} ) {

    const [submitLoading, setSubmitLoading] = useState(false)
    const [cancelButtonDisable, setCancelButtonDisable] = useState(false)
    const [applicationVersion, setApplicationVersion] = useState("1.0.0")

    return (
        <>
            <Modal
                title="新建版本"
                open={open}
                onOk={async () => {
                    // 1.0 validated content.
                    setApplicationVersion(applicationVersion.trim())
                    if (applicationVersion.length === 0) {
                        message.error("应用版本不能为空!")
                        return
                    }
                    // 2.0 submit data with http-client
                    setSubmitLoading(true)
                    setCancelButtonDisable(true)
                    const res = await http.post<ApplicationVersion>("app/version/create", {
                        applicationId: application?.id,
                        version: applicationVersion,
                        config: JSON.stringify({
                            version: applicationVersion
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
                        <Input required placeholder="版本号: 1.0.0" value={applicationVersion} onChange={(e) => {
                            setApplicationVersion(e.target.value)
                        }}/>
                    </div>
                </div>
            </Modal>
        </>
    )
}