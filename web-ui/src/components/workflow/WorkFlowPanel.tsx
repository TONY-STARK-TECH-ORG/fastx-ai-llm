import {
    ReactFlow,
    MiniMap,
    Controls,
    Background,
    useNodesState,
    useEdgesState,
    addEdge,
    Panel, useReactFlow, ReactFlowProvider,
    Node,
    Edge,
    OnConnect,
    NodeTypes,
    FitViewOptions,
    DefaultEdgeOptions,
    useStoreApi
} from '@xyflow/react';
// Layout
import Dagre from '@dagrejs/dagre';
import DevTools from "./dev/DevTools.tsx";
import '@xyflow/react/dist/style.css';
import {useCallback} from "react";

import SimpleNode from "./SimpleNode.tsx";
import SimpleEdge from "./SimpleEdge.tsx";

export const initialNodes:Node[] = [
    {
        id: '1',
        type: 'input',
        data: { label: 'input' },
        position: { x: 0, y: 0 },
    },
    {
        id: '2',
        data: { label: 'node 2' },
        position: { x: 0, y: 100 },
    },
    {
        id: '2a',
        data: { label: 'node 2a' },
        position: { x: 0, y: 200 },
    },
    {
        id: '2b',
        data: { label: 'node 2b' },
        position: { x: 0, y: 300 },
    },
    {
        id: '2c',
        data: { label: 'node 2c' },
        position: { x: 0, y: 400 },
    },
    {
        id: '2d',
        data: { label: 'node 2d' },
        position: { x: 0, y: 500 },
    },
    {
        id: '3',
        data: { label: 'node 3' },
        position: { x: 200, y: 100 },
    },
];

export const initialEdges:Edge[] = [
    { id: 'e12', source: '1', target: '2', animated: true },
    { id: 'e13', source: '1', target: '3', animated: true },
    { id: 'e22a', source: '2', target: '2a', animated: true },
    { id: 'e22b', source: '2', target: '2b', animated: true },
    { id: 'e22c', source: '2', target: '2c', animated: true },
    { id: 'e2c2d', source: '2c', target: '2d', animated: true },
];

const fitViewOptions: FitViewOptions = {
    padding: 0.2,
};

const defaultEdgeOptions: DefaultEdgeOptions = {
    animated: true,
};

const MIN_DISTANCE = 150;

const getLayoutedElements = (nodes, edges, options) => {
    const g = new Dagre.graphlib.Graph().setDefaultEdgeLabel(() => ({}));
    g.setGraph({ rankdir: options.direction });

    edges.forEach((edge) => g.setEdge(edge.source, edge.target));
    nodes.forEach((node) =>
        g.setNode(node.id, {
            ...node,
            width: node.measured?.width ?? 0,
            height: node.measured?.height ?? 0,
        }),
    );

    Dagre.layout(g);

    return {
        nodes: nodes.map((node) => {
            const position = g.node(node.id);
            // We are shifting the dagre node position (anchor=center center) to the top left
            // so it matches the React Flow node anchor point (top left).
            const x = position.x - (node.measured?.width ?? 0) / 2;
            const y = position.y - (node.measured?.height ?? 0) / 2;

            return { ...node, position: { x, y } };
        }),
        edges,
    };
};

export function LayoutFlow () {

//------------------------------------------------------------
// register type start!
//------------------------------------------------------------
    const nodeTypes: NodeTypes = {
        // general node.
        simpleNode: SimpleNode
    }

    const edgeTypes = {
        simpleEdge: SimpleEdge
    }
//------------------------------------------------------------
// register type end!
//------------------------------------------------------------

    const { fitView, getInternalNode } = useReactFlow();
    const store = useStoreApi();

    const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
    const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);

    const onConnect: OnConnect = useCallback((connection) => {
        const edge = { ...connection, type: 'simpleEdge' };
        setEdges((eds) => addEdge(edge, eds));
    }, [setEdges]);

    const onLayout = useCallback(
        (direction) => {
            console.log(nodes);
            const layouted = getLayoutedElements(nodes, edges, { direction });

            setNodes([...layouted.nodes]);
            setEdges([...layouted.edges]);

            window.requestAnimationFrame(() => {
                fitView();
            });
        },
        [nodes, edges],
    );

    const getClosestEdge = useCallback((node) => {
        const { nodeLookup } = store.getState();
        const internalNode = getInternalNode(node.id);

        const closestNode = Array.from(nodeLookup.values()).reduce(
            (res, n) => {
                if (n.id !== internalNode.id) {
                    const dx =
                        n.internals.positionAbsolute.x -
                        internalNode.internals.positionAbsolute.x;
                    const dy =
                        n.internals.positionAbsolute.y -
                        internalNode.internals.positionAbsolute.y;
                    const d = Math.sqrt(dx * dx + dy * dy);

                    if (d < res.distance && d < MIN_DISTANCE) {
                        res.distance = d;
                        res.node = n;
                    }
                }

                return res;
            },
            {
                distance: Number.MAX_VALUE,
                node: null,
            },
        );

        if (!closestNode.node) {
            return null;
        }

        const closeNodeIsSource =
            closestNode.node.internals.positionAbsolute.x <
            internalNode.internals.positionAbsolute.x;

        return {
            id: closeNodeIsSource
                ? `${closestNode.node.id}-${node.id}`
                : `${node.id}-${closestNode.node.id}`,
            source: closeNodeIsSource ? closestNode.node.id : node.id,
            target: closeNodeIsSource ? node.id : closestNode.node.id,
        };
    }, []);

    const onNodeDrag = useCallback(
        (_, node) => {
            const closeEdge = getClosestEdge(node);

            setEdges((es) => {
                const nextEdges = es.filter((e) => e.className !== 'temp');

                if (
                    closeEdge &&
                    !nextEdges.find(
                        (ne) =>
                            ne.source === closeEdge.source && ne.target === closeEdge.target,
                    )
                ) {
                    closeEdge.className = 'temp';
                    nextEdges.push(closeEdge);
                }

                return nextEdges;
            });
        },
        [getClosestEdge, setEdges],
    );

    const onNodeDragStop = useCallback(
        (_, node) => {
            const closeEdge = getClosestEdge(node);

            setEdges((es) => {
                const nextEdges = es.filter((e) => e.className !== 'temp');

                if (
                    closeEdge &&
                    !nextEdges.find(
                        (ne) =>
                            ne.source === closeEdge.source && ne.target === closeEdge.target,
                    )
                ) {
                    nextEdges.push(closeEdge);
                }

                return nextEdges;
            });
        },
        [getClosestEdge],
    );

    return (
        <div className="w-full h-full flex flex">
            <div className="grow">
                <ReactFlow
                    nodes={nodes}
                    edges={edges}
                    onNodesChange={onNodesChange}
                    onEdgesChange={onEdgesChange}
                    onConnect={onConnect}
                    fitView
                    nodeTypes={nodeTypes}
                    edgeTypes={edgeTypes}
                    fitViewOptions={fitViewOptions}
                    defaultEdgeOptions={defaultEdgeOptions}

                    onNodeDrag={onNodeDrag}
                    onNodeDragStop={onNodeDragStop}
                >
                    <Panel position="top-center">
                        <button onClick={() => onLayout('TB')}>vertical layout</button>
                        <button onClick={() => onLayout('LR')}>horizontal layout</button>
                    </Panel>
                    <Controls/>
                    <MiniMap zoomable pannable position={"top-left"}/>
                    <Background variant={"dots"} color="#ddd" gap={24} size={2}/>
                    <DevTools/>
                </ReactFlow>
            </div>
            <div className="w-[200px] h-full border border-gray-100 bg-white hover:shadow hover:shadow-blue-50 ml-2">

            </div>
        </div>
    )
}

export default function WorkflowPanel() {
    return (
        <div className="w-full h-full">
            <ReactFlowProvider>
                <LayoutFlow/>
            </ReactFlowProvider>
        </div>

    );
}