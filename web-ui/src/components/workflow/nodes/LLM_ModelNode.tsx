import {useState} from 'react';
import {Handle, Node, NodeProps, NodeToolbar, Position} from '@xyflow/react';
import {Tool} from "../../../store/tool/Tool.ts";
import {OrgTool} from "../../../store/define.ts";
import {Button, ConfigProvider, Form, Input} from "antd";
import {CloseOutlined, PlaySquareOutlined} from "@ant-design/icons";
import useWorkflowStore from "../../../store/WorkflowStore.ts";

const handleStyle = {};

export type LLM_ModelNode = Node<
    {
        tool?: Tool;
        orgTool?: OrgTool;
        name: string;
        innerData: any;
        executeResult: any | undefined;
    },
    'llm-model'
>;

export default function LLM_ModelNode(props: NodeProps<LLM_ModelNode>) {

    const [form] = Form.useForm();
    const [tool] = useState(props.data?.tool);
    // const [orgTool] = useState(props.data?.orgTool);
    const [nodes, setNodes] = useWorkflowStore(state => [state.nodes, state.setNodes]);

    const updateNodeName = (event: React.ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value;
        setNodes(nodes.map(node => {
            if (node.id === props.id) {
                node.data.name = value;
            }
            return node;
        }))
    }

    const renderLLMInputs = () => {
        const inputs = JSON.parse(tool?.prototype ?? "{\"inputs\": []}").inputs;
        return (<>
            {inputs.map((c: any, _index: number) => {
                if (!c.array) {
                    return (
                        <Form.Item labelAlign={"right"}  key={c.name} className="m-1" label={c.name} name={['inputs', c.name]}
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
                                        <div key={subField.key} className="bg-gray-50 flex flex-col p-1">
                                            <CloseOutlined className="w-[10px] h-[10px]"
                                                onClick={() => {
                                                    subOpt.remove(subField.name);
                                                }}
                                            />
                                            <Form.Item labelAlign={"right"} className="m-1" label={"role"}
                                                       name={[subField.name, 'role']}
                                                       rules={[{required: c.required, message: '请输入'}]}>
                                                <Input placeholder="role"/>
                                            </Form.Item>
                                            <Form.Item labelAlign={"right"}  className="m-1 grow" label={"content"}
                                                       name={[subField.name, 'content']}
                                                       rules={[{required: c.required, message: '请输入'}]}>
                                                <Input placeholder="content"/>
                                            </Form.Item>
                                        </div>
                                    ))}
                                    <Button size="small" className="mt-1" type="dashed" onClick={() => subOpt.add()} block>
                                        <p className="text-xs font-thin">+ 添加消息</p>
                                    </Button>
                                </div>
                            )}
                        </Form.List>
                    )
                }
            })}
        </>)
    }

    return (
        <div className={
            "bg-white w-[200px]rounded border border-gray-100 " + (props.selected && "border-red-500/50")
        }>
            <NodeToolbar
                className="top-3"
                isVisible={props.selected}
                position={Position.Top}
            >
                <Button size="small" type="text" icon={<PlaySquareOutlined className="text-lime-600" />} />
            </NodeToolbar>
            <div className="w-full flex-col items-start h-full">
                <div className="w-full flex p-2 items-center">
                    <p className="text-xs w-[45px]">标识：</p>
                    <Input defaultValue={props.data.name} size="small" onChange={updateNodeName} />
                </div>
                <div className="w-full h-0.5 bg-gray-50"></div>
                <div className="w-full flex p-2 items-center">
                    <img src={tool?.icon} className="w-[20px] h-[20px] border border-gray-100"/>
                    <p className="text-xs font-medium ml-1">{tool?.name}</p>
                    <div className="grow"></div>
                    <p className="text-[10px] font-thin ml-1 text-orange-500">v {tool?.version}</p>
                </div>
                <div className="w-full h-0.5 bg-gray-50"></div>
                <div className="w-full flex p-2">
                    <ConfigProvider
                        theme={{
                            components: {
                                Form: {
                                    labelFontSize: 10,
                                    labelColonMarginInlineEnd: 0,
                                    labelColonMarginInlineStart: 0
                                },
                            },
                        }}
                    >
                        <Form
                            onFieldsChange={(_changedFields, _allFields) => {
                                props.data.innerData = form.getFieldsValue()
                            }}
                            initialValues={props.data.innerData ?? {}}
                            colon={false}
                            size="small"
                            form={form}
                            name="tool_detail_form"
                            autoComplete="off"
                        >
                            {renderLLMInputs()}
                        </Form>
                    </ConfigProvider>
                </div>
                <div className="w-full h-0.5 bg-gray-50"></div>
                {props.data.executeResult && props.data.executeResult != -1 ? (
                    <div className="p-1">
                        <p className="text-[8px] text-gray-400">上次执行结果</p>
                    </div>
                ) : null}
            </div>
            <Handle
                className="!bg-red-500 !w-2 !h-3 !rounded-[2px]"
                type="source"
                position={Position.Right}
                id="output"
                style={handleStyle}
            />
            <Handle
                className="!bg-red-500 !w-2 !h-3 !rounded-[2px]"
                type="target"
                position={Position.Left}
                id="input"
                style={handleStyle}
                isConnectable={true}
            />
        </div>
    );
}