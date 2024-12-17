import {
    useNodes,
    ViewportPortal,
    useReactFlow,
    type XYPosition,
} from '@xyflow/react';

export default function NodeInspector() {
    const { getInternalNode } = useReactFlow();
    const nodes = useNodes();

    return (
        <ViewportPortal>
            <div className="react-flow__devtools-nodeinspector">
                {nodes.map((node) => {
                    const internalNode = getInternalNode(node.id);
                    if (!internalNode) {
                        return null;
                    }

                    const absPosition = internalNode?.internals.positionAbsolute;

                    return (
                        <NodeInfo
                            key={node.id}
                            id={node.id}
                            selected={!!node.selected}
                            type={node.type || 'default'}
                            position={node.position}
                            absPosition={absPosition}
                            width={node.measured?.width ?? 0}
                            height={node.measured?.height ?? 0}
                            data={node.data}
                        />
                    );
                })}
            </div>
        </ViewportPortal>
    );
}

type NodeInfoProps = {
    id: string;
    type: string;
    selected: boolean;
    position: XYPosition;
    absPosition: XYPosition;
    width?: number;
    height?: number;
    data: any;
};

function NodeInfo({
                      id,
                      type,
                      selected,
                      position,
                      absPosition,
                      width,
                      height,
                      data,
                  }: NodeInfoProps) {
    if (!width || !height) {
        return null;
    }

    return (
        <div
            className="react-flow__devtools-nodeinfo"
            style={{
                position: 'absolute',
                transform: `translate(${absPosition.x}px, ${absPosition.y + height}px)`,
                width: width * 2,
            }}
        >
            <p className="text-[8px]">id: {id}</p>
            <p className="text-[8px]">type: {type}</p>
            <p className="text-[8px]">selected: {selected ? 'true' : 'false'}</p>
            <p className="text-[8px]">
                position: {position.x.toFixed(1)}, {position.y.toFixed(1)}
            </p>
            <p className="text-[8px]">
                dimensions: {width} × {height}
            </p>
            <p className="text-[8px]">data: {JSON.stringify(data, null, 2)}</p>
        </div>
    );
}