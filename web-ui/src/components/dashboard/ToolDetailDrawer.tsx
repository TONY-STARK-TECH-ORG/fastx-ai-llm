import {ReactNode, useEffect, useState} from 'react';
import {Button, Divider, Drawer, Form, Input, message, Select, Space} from 'antd';
import {Tool} from "../../store/tool/Tool.ts";
import {OrgTool} from "../../store/define.ts";
import ReactJsonView from '@microlink/react-json-view'
import {http} from "../../api/Http.ts";
import {SlidingWindow} from "../../utils/SlidingWindow.ts";
import {useOrganizationStore} from "../../store/OrganizationStore.ts";
import {CloseOutlined} from "@ant-design/icons";

type ToolDetailDrawerProps = {open:boolean; current: Tool | undefined; orgTools: OrgTool[] | undefined; onClose: ()=>void; onRefresh: ()=>void }

export default function ToolDetailDrawer(
    {open, current, orgTools, onClose, onRefresh}:
    ToolDetailDrawerProps
) {

    const [form] = Form.useForm();

    const [output, setOutput] = useState<any | undefined>({})
    const [saveLoading, setSaveLoading] = useState(false)
    const [orgId, orgName] = useOrganizationStore(state => [state.id, state.name])

    const [configFormItem, setConfigFormItem] = useState<ReactNode>()
    const [inputsFormItem, setInputsFormItem] = useState<ReactNode>()

    // already has this tool.
    const haveThisTool = () => {
        return orgTools?.find(o => o.toolCode === current?.code && o.toolVersion === current?.version)
    }

    const sendToolTestRequest = async () => {
        try {
            const formValue = await form.validateFields();

            const requestBody = {
                toolCode: current?.code,
                toolVersion: current?.version,
                type: current?.type,
                input: formValue
            };

            if (formValue && formValue.config.streaming == 'true') {
                setOutput({
                    streaming: "",
                    response: ""
                })
                const slideWindow = new SlidingWindow("<FASTX-EOF>")
                await http.stream("tool/platform/tool/stream-exec", requestBody, (data, stop) => {
                    // test match
                    slideWindow.processChunk(data ?? "", (before) => {
                        setOutput((prev: any) => {
                            const streaming = prev.streaming + before;
                            return {
                                ...prev,
                                streaming: streaming
                            }
                        })
                    }, (after) => {
                        setOutput((prev: any) => {
                            const response = prev.response + after;
                            return {
                                ...prev,
                                response: response
                            }
                        })
                    })

                    if (stop) {
                        if (!slideWindow.isFoundMarker()) {
                            setOutput((prev: any) => {
                                const streaming = prev.streaming + slideWindow.checkStop();
                                return {
                                    ...prev,
                                    streaming: streaming
                                }
                            })
                        }
                        message.success("流输出结束")
                    }
                });
            } else {
                const res = await http.post<any>(
                    "tool/platform/tool/exec", requestBody)
                if (res.success && res.data.success) {
                    message.success("工具调用成功")
                    const data = res.data
                    console.log(JSON.parse(data.data!!))
                    setOutput(JSON.parse(data.data!!))
                } else {
                    message.error("工具调用失败" + res.data.error);
                }
            }
        } catch (errorInfo) {
            console.log(errorInfo)
            message.error("检查表单输入内容")
        }
    }

    const onSave = async () => {
        console.log(form.getFieldsValue())
        setSaveLoading(true)
        // save tool config to org tool.
        try {
            const formValue = await form.validateFields();

            const res = await http.post("tool/org/tool/create", {
                organizationId: orgId,
                toolCode: current?.code,
                toolVersion: current?.version,
                configData: JSON.stringify(formValue.config),
            });
            if(res.success) {
                message.success("工具配置保存成功")
                // refresh outer page.
                onRefresh()
            } else {
                message.error("工具配置保存失败: " + res.msg)
            }
        } catch (errorInfo) {
            message.error("请检查配置项内容是否填写完整、测试验证后，再做保存操作")
        }

        setSaveLoading(false)
    }

    const renderConfigForm = () => {
        const config = JSON.parse(current?.prototype ?? "{\"config\": []}").config;
        config.forEach((cnf: any) => cnf.key = cnf.name)
        const item = (<>
            {config.map((c: any, _index: number) => {
                return (
                    <Form.Item key={c.name} className="m-1" label={c.name} name={['config', c.name]}
                               rules={[{required: c.required, message: '请输入' + c.name}]}>
                        <Input/>
                    </Form.Item>
                )
            })}
        </>)
        setConfigFormItem(item)
    }

    const renderInputsForm = () => {
        const inputs = JSON.parse(current?.prototype ?? "{\"inputs\": []}").inputs;
        inputs.forEach((inp: any) => inp.key = inp.name);
        const item = (<>
            {inputs.map((c: any, _index: number) => {
                if (!c.array) {
                    return (
                        <Form.Item key={c.name} className="m-1" label={c.name} name={['inputs', c.name]}
                                   rules={[{required: c.required, message: '请输入' + c.name}]}>
                            <Input/>
                        </Form.Item>
                    )
                } else {
                    return (
                        <Form.List key={c.name} name={['inputs', c.name]}>
                            {(subFields, subOpt) => (
                                <div>
                                    {subFields.map((subField) => (
                                        <div key={subField.key} className="bg-gray-50 flex flex-row-reverse pl-1">
                                            <Form.Item className="m-1" label={"role"}
                                                       name={[subField.name, 'role']}
                                                       rules={[{required: c.required, message: '请输入'}]}>
                                                <Input placeholder="role"/>
                                            </Form.Item>
                                            <Form.Item className="m-1 grow" label={"content"}
                                                       name={[subField.name, 'content']}
                                                       rules={[{required: c.required, message: '请输入'}]}>
                                                <Input placeholder="content"/>
                                            </Form.Item>
                                            <CloseOutlined
                                                onClick={() => {
                                                    subOpt.remove(subField.name);
                                                }}
                                            />
                                        </div>
                                    ))}
                                    <Button className="mt-1" type="dashed" onClick={() => subOpt.add()} block>
                                        + 添加消息
                                    </Button>
                                </div>
                            )}
                        </Form.List>
                    )
                }
            })}
        </>)
        setInputsFormItem(item)
    }

    useEffect(() => {
        if (current) {
            renderConfigForm()
            renderInputsForm()
        }
    }, [current]);

    return (
        <Drawer
            title={current?.name}
            width="50%"
            onClose={onClose}
            open={open}
            styles={{
                body: {
                    padding: 8
                },
            }}
            extra={
                <Space>
                    <Button onClick={onClose}>返回</Button>
                    <Button loading={saveLoading} onClick={onSave} type="primary">
                        保存
                    </Button>
                </Space>
            }
        >
            <div className="flex flex-col">
                <Form
                    labelCol={{span: 6}}
                    wrapperCol={{span: 18}}
                    form={form}
                    name="tool_detail_form"
                    autoComplete="off"
                    initialValues={{config: haveThisTool()?.configData ? JSON.parse(haveThisTool()?.configData!!) : {}}}
                >
                    <p>配置项</p>
                    <Form.Item initialValue={orgId} label="组织" className="m-0" name={['config', 'organizationId']} required
                               rules={[{required: true}]}>
                        <Select
                            placeholder="工具所属组织"
                            disabled
                            options={[{
                                value: orgId,
                                label: orgName
                            }]}
                        />
                    </Form.Item>
                    {configFormItem}
                    <Divider className="!my-2"/>
                    <p>测试输入 <span className="text-xs font-medium text-red-500">如配置包含密钥，为保护密钥安全，需重新设置后进行测试</span></p>
                    {inputsFormItem}
                </Form>
                <Button className="mt-2" onClick={sendToolTestRequest}>发起测试</Button>
                <Divider className="!my-2"/>
                <div className="grow w-full">
                    <div className="w-full overflow-auto">
                        <ReactJsonView src={output}/>
                    </div>
                </div>
            </div>
        </Drawer>
    )
}