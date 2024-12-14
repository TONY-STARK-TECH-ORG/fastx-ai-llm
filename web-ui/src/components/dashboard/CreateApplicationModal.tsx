import {Modal, Input, Select, message} from "antd";
import {useContext, useState} from "react";
import {http} from "../../api/Http.ts";
import {UserContext} from "../../context/UserContext.ts";

const { TextArea } = Input;
export default function CreateApplicationModal({open, onCancel, onSuccess}: {open: boolean; onCancel: ()=>void; onSuccess: ()=>void} ) {

    const [selectedOption, setSelectedOption] = useState("agent")
    const [submitLoading, setSubmitLoading] = useState(false)
    const [cancelButtonDisable, setCancelButtonDisable] = useState(false)

    const [applicationName, setApplicationName] = useState("")
    const [applicationDesc, setApplicationDesc] = useState("")

    const [selectedOrganization, setSelectedOrganization] = useState(undefined)

    const { organization } = useContext(UserContext);

    return (
        <>
            <Modal
                title="新建应用"
                open={open}
                onOk={async () => {
                    // 1.0 validated content.
                    setApplicationName(applicationName.trim())
                    if (applicationName.length === 0) {
                        message.error("应用名称不能为空!")
                        return
                    }
                    setApplicationDesc(applicationDesc.trim())
                    if (applicationDesc.length > 100) {
                        message.error("应用简介不能超过100字!")
                        return
                    }
                    if (applicationDesc.length === 0) {
                        message.error("请输入简短的应用描述")
                        return
                    }
                    if (selectedOrganization === undefined) {
                        message.error("请选择应用所属组织")
                        return
                    }
                    // 2.0 submit data with http-client
                    setSubmitLoading(true)
                    setCancelButtonDisable(true)
                    const res = await http.post("app/create", {
                        name: applicationName,
                        description: applicationDesc,
                        type: selectedOption,
                        iconUrl: "https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png",
                        organizationId: selectedOrganization
                    })
                    if (res.success) {
                        message.success("应用创建成功")
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
                        <Input required placeholder="应用名称" value={applicationName} onChange={(e) => {
                            setApplicationName(e.target.value)
                        }}/>
                        <Select
                            className="mt-2 w-full"
                            defaultValue={selectedOption}
                            onChange={(value) => {
                                setSelectedOption(value)
                            }}
                            options={[
                                {value: 'agent', label: '智能体'},
                                {value: 'playground', label: '体验场'},
                            ]}
                        />
                    </div>
                    <Select
                        className="mt-2"
                        placeholder="选择应用所属组织"
                        options={organization?.map(item => {
                            return {
                                value: item.id,
                                label: item.name
                            }
                        })}
                        onChange={(value) => {
                            setSelectedOrganization(value)
                        }}
                    />
                    <TextArea required showCount className="mt-2" rows={4} placeholder="应用简介 (100字内)" maxLength={105} value={applicationDesc} onChange={(e) => {
                        setApplicationDesc(e.target.value)
                    }}/>
                </div>
            </Modal>
        </>
    )
}