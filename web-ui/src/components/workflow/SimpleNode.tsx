import {useCallback, useState} from 'react';
import {Handle, Position} from '@xyflow/react';
import { NodeProps, Node } from '@xyflow/react';

// @TODO (stark) https://reactflow.dev/examples/nodes/node-toolbar 工具条
// @TODO (stark) https://reactflow.dev/examples/nodes/connection-limit Handler 连接限制
// @TODO (stark) https://reactflow.dev/examples/layout/sub-flows 子节点 组
// @TODO (stark) https://reactflow.dev/examples/interaction/drag-and-drop 拖动新增节点
// @TODO (stark) https://reactflow.dev/examples/interaction/prevent-cycles 防止死循环
// @TODO (stark) https://reactflow.dev/examples/interaction/save-and-restore 保存数据
// @TODO (stark) https://reactflow.dev/examples/styling/styled-components 接口样式
// @TODO (stark) https://reactflow.dev/examples/misc/use-react-flow-hook 放大指定节点到画布中央

const handleStyle = { left: 10 };

export type SimpleNode = Node<
    {
        initialCount?: number;
    },
    'SimpleEdge'
>;

export default function SimpleNode(props: NodeProps<SimpleNode>) {

    const [count, setCount] = useState(props.data?.initialCount ?? 0);
    const onChange = useCallback((evt) => {
        console.log(evt.target.value);
    }, []);

    return (
        <div className="bg-white">
            <Handle type="target" position={Position.Top} />
            <div>
                <label htmlFor="text">Text:</label>
                <input id="text" name="text" onChange={onChange} className="nodrag" />
            </div>
            <Handle type="source" position={Position.Bottom} id="a" />
            <Handle
                type="source"
                position={Position.Bottom}
                id="b"
                style={handleStyle}
            />
        </div>
    );
}