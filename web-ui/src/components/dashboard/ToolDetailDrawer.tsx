import {ReactNode, useState} from 'react';
import {Button, Divider, Drawer, Form, Input, InputNumber, message, Space, Switch} from 'antd';
import {Tool} from "../../store/tool/Tool.ts";
import {OrgTool} from "../../store/define.ts";
import ReactJsonView from '@microlink/react-json-view'
import {http} from "../../api/Http.ts";
import {SlidingWindow} from "../../utils/SlidingWindow.ts";

const { TextArea } = Input;

type ToolDetailDrawerProps = {open:boolean; current: Tool | undefined; orgTools: OrgTool[] | undefined; onClose: ()=>void; onRefresh: ()=>void }

export default function ToolDetailDrawer(
    {open, current, orgTools, onClose, onRefresh}:
    ToolDetailDrawerProps
) {

    const [configForm] = Form.useForm();
    const [inputForm] = Form.useForm();
    const [output, setOutput] = useState<any | undefined>({})

    // already has this tool.
    const haveThisTool = () => {
        return orgTools?.find(o => o.toolCode === current?.code && o.toolVersion === current?.version)
    }

    const getConfig = () => {
        if (!current?.prototype) {
            return
        }
        const protoType = JSON.parse(current?.prototype);
        if (!protoType.config) {
            return
        }
        const config = protoType.config
        const configFormItems:ReactNode[] = []
        // @TODO (stark) need a object to parse this JSON. and render with object, not use json string.
        config.forEach((c: any) => {
            if (c.type.endsWith("String")) {
                configFormItems.push((
                    <Form.Item key={c.name} className="m-0 mt-1" name={c.name} rules={[{ required: !!c.required }]}>
                        <Input className="mt-2" placeholder={c.name}/>
                    </Form.Item>
                ))
            } else if (c.type.endsWith("Integer") || c.type.endsWith("Long") || c.type.endsWith("Float") || c.type.endsWith("Double")) {
                configFormItems.push((
                    <Form.Item key={c.name} className="m-0 mt-1" name={c.name} rules={[{ required: !!c.required }]}>
                        <InputNumber className="mt-2" placeholder={c.name}/>
                    </Form.Item>
                ))
            } else if (c.type.endsWith("Boolean")) {
                configFormItems.push((
                    <Form.Item key={c.name} className="m-0 mt-1" label={c.name} name={c.name} rules={[{ required: !!c.required }]}>
                        <Switch className="mt-2" />
                    </Form.Item>
                ))
            }
        })
        return configFormItems;
    }

    const getInputConfig = () => {
        if (!current?.prototype) {
            return
        }
        const protoType = JSON.parse(current?.prototype);
        if (!protoType.inputs) {
            return
        }
        const input = protoType.inputs
        const inputFormItems:ReactNode[] = []
        // @TODO (stark) need a object to parse this JSON. and render with object, not use json string.
        input.forEach((c: any) => {
            if (current.type === 'llm-model' && c.name === 'messages') {
                return ;
            }
            if (c.type.endsWith("String")) {
                inputFormItems.push((
                    <Form.Item key={c.name} className="m-0 mt-1" name={c.name} rules={[{ required: !!c.required }]}>
                        <Input className="mt-2" placeholder={c.name}/>
                    </Form.Item>
                ))
            } else if (c.type.endsWith("Integer") || c.type.endsWith("Long") || c.type.endsWith("Float") || c.type.endsWith("Double")) {
                inputFormItems.push((
                    <Form.Item key={c.name} className="m-0 mt-1" name={c.name} rules={[{ required: !!c.required }]}>
                        <InputNumber className="mt-2" placeholder={c.name}/>
                    </Form.Item>
                ))
            } else if (c.type.endsWith("Boolean")) {
                inputFormItems.push((
                    <Form.Item key={c.name} className="m-0 mt-1" name={c.name} rules={[{ required: !!c.required }]}>
                        <Switch className="mt-2" checkedChildren={c.name} unCheckedChildren={c.name} />
                    </Form.Item>
                ))
            } else {
                inputFormItems.push((
                    <Form.Item key={c.name} className="m-0 mt-1" name={c.name} rules={[{ required: !!c.required }]}>
                        <TextArea placeholder={c.name + "{} 请输入合法的 JSON 内容"} />
                    </Form.Item>
                ))
            }
        })
        if (current.type === 'llm-model') {
            // add messages input item
            inputFormItems.push((
                <Form.Item key="messages" className="m-0 mt-1" name="messages" rules={[{ required: true }]}>
                    <Input placeholder={"请输入提示词"} />
                </Form.Item>
            ))
        }
        return inputFormItems;
    }

    const sendToolTestRequest = async () => {
        try {
            const configValues = await configForm.validateFields();
            const inputValues = await inputForm.validateFields();

            const requestBody = {
                toolCode: current?.code,
                toolVersion: current?.version,
                type: current?.type,
                input: {
                    config: configValues,
                    data: {
                        ...inputValues,
                        messages: current?.type === 'llm-model' ? [{
                            role: 'user',
                            content: inputValues.messages
                        }] : inputValues.messages
                    }
                }
            };

            if (configValues && configValues.streaming) {
                setOutput({
                    streaming: "",
                    response: ""
                })
                const slideWindow = new SlidingWindow("<FASTX-EOF>")
                await http.stream("tool/platform/tool/stream-exec", requestBody, (data, stop) => {
                    console.log(data)

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
                        message.success("流输出结束")
                        setOutput({
                            ...output,
                            responseObj: JSON.parse(output.response)
                        })
                    }
                });
            } else {
                const res = await http.post<any>(
                    "tool/platform/tool/exec", requestBody)
                if (res.success) {
                    message.success("工具调用成功")
                    const data = res.data
                    console.log(JSON.parse(data.data!!))
                    setOutput(JSON.parse(data.data!!))
                } else {
                    message.error("工具调用失败" + res.msg);
                }
            }
        } catch (errorInfo) {
            console.log(errorInfo)
            message.error("检查表单输入内容")
        }
    }

    return (
        <Drawer
            title={current?.name}
            width={400}
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
                    <Button onClick={onClose} type="primary">
                        保存
                    </Button>
                </Space>
            }
        >
            <div className="flex flex-col">
                <div>
                    <h1>配置项</h1>
                    <Form initialValues={haveThisTool() ? JSON.parse(haveThisTool()!!.configData) : {}} form={configForm} layout="vertical">
                        {getConfig()}
                    </Form>
                </div>
                <Divider className="!my-2"/>
                <div>
                    <h1>测试输入</h1>
                    <Form form={inputForm} layout="vertical">
                        {getInputConfig()}
                    </Form>
                    <Button className="mt-2" onClick={sendToolTestRequest}>发起测试</Button>
                </div>
                <Divider className="!my-2"/>
                <div className="grow">
                    <h1>测试输出</h1>
                    <div>
                        <ReactJsonView src={output}/>
                    </div>
                </div>
            </div>
        </Drawer>
    )
}