import React, {useCallback, useState} from 'react';
import {Node, NodeProps, NodeToolbar, Position} from '@xyflow/react';
import SimpleHandle from "./SimpleHandle.tsx";

const handleStyle = { left: 10 };

export type SimpleNode = Node<
    {
        initialCount?: number;
    },
    'SimpleEdge'
>;

export default function SimpleNode(props: NodeProps<SimpleNode>) {

    const [count, setCount] = useState(props.data?.initialCount ?? 0);
    const onChange = useCallback((evt: React.ChangeEvent<HTMLInputElement>) => {
        console.log(evt.target.value);
    }, []);

    return (
        <div className="bg-white">
            <NodeToolbar
                isVisible={true}
                position={Position.Left}
            >
                <button>cut</button>
                <button>copy</button>
                <button>paste</button>
            </NodeToolbar>
            <div>
                <label htmlFor="text">Text:</label>
                <input id="text" name="text" onChange={onChange} className="nodrag" />
            </div>
            <SimpleHandle
                className="!bg-red-500 !w-2 !h-3 !rounded-[2px]"
                connectionCount={1}
                type="source"
                position={Position.Bottom}
                id="b"
                style={handleStyle}
            />
        </div>
    );
}