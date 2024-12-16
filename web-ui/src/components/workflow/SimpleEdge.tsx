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
                    strokeWidth: 1,
                    stroke: '#eeeeee',
                }}
            />
            <circle r="2" fill="#ff0073">
                <animateMotion dur="3s" repeatCount="indefinite" path={edgePath}/>
            </circle>
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