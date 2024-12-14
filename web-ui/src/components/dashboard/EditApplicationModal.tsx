import {Modal, Input, Select, message} from "antd";
import {useContext, useEffect, useState} from "react";
import {http} from "../../api/Http.ts";
import {UserContext} from "../../context/UserContext.ts";
import {Application} from "../../store/define.ts";

const { TextArea } = Input;
export default function EditApplicationModal(
    {application, open, onCancel, onSuccess}:
    {application: Application | undefined; open: boolean; onCancel: ()=>void; onSuccess: ()=>void} ) {

    const [selectedOption, setSelectedOption] = useState<string | undefined>("agent")
    const [submitLoading, setSubmitLoading] = useState(false)
    const [cancelButtonDisable, setCancelButtonDisable] = useState(false)

    const [applicationName, setApplicationName] = useState<string | undefined>(undefined)
    const [applicationDesc, setApplicationDesc] = useState<string | undefined>(undefined)

    const [selectedOrganization, setSelectedOrganization] = useState<string | undefined>(undefined)

    const { organization } = useContext(UserContext);

    useEffect(() => {
        if (open) {
            setSelectedOption(application?.type)
            setApplicationDesc(application?.description)
            setApplicationName(application?.name)
            setSelectedOrganization(application?.organization.id)
        }

        return () => {
            setApplicationName(undefined)
            setApplicationDesc(undefined)
            setSelectedOption("agent")
        }
    }, [open])

    return (
        <>
            <Modal
                title="编辑应用"
                open={open}
                onOk={async () => {
                    // 1.0 validated content.
                    setApplicationName(applicationName?.trim())
                    if (applicationName?.length === 0) {
                        message.error("应用名称不能为空!")
                        return
                    }
                    setApplicationDesc(applicationDesc?.trim())
                    if (applicationDesc && applicationDesc.length > 100) {
                        message.error("应用简介不能超过100字!")
                        return
                    }
                    if (applicationDesc?.length === 0) {
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
                    const res = await http.post("app/update", {
                        id: application?.id,
                        name: applicationName,
                        description: applicationDesc,
                        type: selectedOption,
                        iconUrl: "https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png",
                        organizationId: selectedOrganization
                    })
                    if (res.success) {
                        message.success("应用更新成功")
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
                        defaultValue={selectedOrganization}
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