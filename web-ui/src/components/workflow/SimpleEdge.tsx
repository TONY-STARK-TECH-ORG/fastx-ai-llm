import {
    BaseEdge,
    EdgeLabelRenderer,
    getStraightPath,
    useReactFlow,
    Edge,
    EdgeProps
} from '@xyflow/react';

// getBezierPath
// getSimpleBezierPath
// getSmoothStepPath
// getStraightPath

// @TODO (stark) https://reactflow.dev/examples/edges/animating-edges 动画处理
// @TODO (stark) https://reactflow.dev/examples/edges/custom-connectionline 连接线处理
// @TODO (stark) https://reactflow.dev/examples/edges/markers 边缘标记（箭头）
// @TODO (stark) https://reactflow.dev/examples/edges/edge-label-renderer 标签渲染器

export type SimpleEdge = Edge<
    {

    },
    'SimpleEdge'
>

export default function SimpleEdge(props: EdgeProps<SimpleEdge>) {
    const { setEdges } = useReactFlow();

    const [edgePath, labelX, labelY] = getStraightPath({
        ...props
    });

    return (
        <>
            <BaseEdge id={props.id} path={edgePath} />
            <EdgeLabelRenderer>
                <button
                    style={{
                        width: '20px',
                        height: '100px',
                        position: 'absolute',
                        transform: `translate(-50%, -50%) translate(${labelX}px, ${labelY}px)`,
                        pointerEvents: 'all',
                        display: 'block'
                    }}
                    className="nodrag nopan"
                    onClick={() => setEdges((edges) => edges.filter((e) => e.id !== props.id))}
                >
                    delete
                </button>
            </EdgeLabelRenderer>
        </>
    );
}