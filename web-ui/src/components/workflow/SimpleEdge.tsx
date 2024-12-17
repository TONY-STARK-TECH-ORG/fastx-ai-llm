import {
    BaseEdge,
    EdgeLabelRenderer,
    useReactFlow,
    Edge,
    EdgeProps, getBezierPath
} from '@xyflow/react';

export type SimpleEdge = Edge<
    {

    },
    'simpleEdge'
>

export default function SimpleEdge(props: EdgeProps<SimpleEdge>) {
    const { setEdges } = useReactFlow();

    const [edgePath, labelX, labelY] = getBezierPath({
        ...props
    });

    return (
        <>
            <BaseEdge
                id={props.id}
                path={edgePath}
                style={props.selected ? {
                    strokeWidth: 2,
                    stroke: '#FF0072',
                }: {
                    strokeWidth: 2,
                    stroke: '#cccccc',
                }}
            />
            <circle r="2" fill="#ff0073">
                <animateMotion dur="3s" repeatCount="indefinite" path={edgePath}/>
            </circle>
            <EdgeLabelRenderer>
                <div
                    className="nodrag nopan hover:cursor-not-allowed"
                    style={{
                        width: '5px',
                        height: '5px',
                        position: 'absolute',
                        transform: `translate(-50%, -50%) translate(${labelX}px, ${labelY}px)`,
                        pointerEvents: 'all',
                        display: 'block',
                        backgroundColor: '#FF6A00'
                    }}
                    onClick={() => setEdges((edges) => edges.filter((e) => e.id !== props.id))}
                >
                </div>
            </EdgeLabelRenderer>
        </>
    );
}