import {
    Background,
    BackgroundVariant,
    Controls,
    Edge,
    MiniMap,
    ReactFlow,
    useReactFlow,
    useStoreApi
} from '@xyflow/react';
// Layout
import DevTools from "./dev/DevTools.tsx";
import '@xyflow/react/dist/style.css';
import useWorkflowStore, {AppNode} from "../../store/WorkflowStore.ts";
import {useEffect} from "react";
import SimpleEdge from "./SimpleEdge.tsx";
import LLM_ModelNode from "./nodes/LLM_ModelNode.tsx";
import {useDrag} from "./DragContext.tsx";
import {v4 as uuidv4} from 'uuid';
import ToolPanel from "./ToolPanel.tsx";
import LLM_FunctionNode from "./nodes/LLM_FunctionNode.tsx";

export type ClosestNode = {distance: number; node: AppNode | undefined }

export default function WorkflowPanel () {
    const [
        relayOut,
        nodes,
        setNodes,
        edges,
        onNodesChange,
        onEdgesChange,
        onConnect,
        onCloseNodeDrag,
        onCloseNodeDragStop,
        zoomCenter,
        isValidConnection,
    ] = useWorkflowStore(state => [
        state.reLayout,
        state.nodes,
        state.setNodes,
        state.edges,
        state.onNodesChange,
        state.onEdgesChange,
        state.onConnect,
        state.onCloseNodeDrag,
        state.onCloseNodeDragStop,
        state.zoomCenter,
        state.isValidConnection
    ]);

    const store = useStoreApi();
    const {
        fitView,
        getInternalNode,
        setCenter,
        screenToFlowPosition
    } = useReactFlow();
    const { type, tool, orgTool } = useDrag()

    const getClosestEdge =  (node: AppNode) => {
        const { nodeLookup } = store.getState()!!;
        const internalNode = getInternalNode(node.id);

        const closestNode:ClosestNode = Array.from(nodeLookup.values()).reduce(
            (res, n) => {
                if (n.id !== internalNode?.id) {
                    const dx = n.internals.positionAbsolute.x - internalNode?.internals.positionAbsolute.x!!;
                    const dy = n.internals.positionAbsolute.y - internalNode?.internals.positionAbsolute.y!!;
                    const d = Math.sqrt(dx * dx + dy * dy);

                    if (d < res.distance && d < 150) {
                        res.distance = d;
                        // @ts-ignore
                        res.node = n;
                    }
                }
                return res;
            },
            {
                distance: Number.MAX_VALUE,
                node: undefined,
            },
        );

        if (!closestNode.node) {return undefined;}
        // @ts-ignore
        const closeNodeIsSource = closestNode.node.internals.positionAbsolute.x < internalNode?.internals.positionAbsolute.x!!;
        return {
            id: closeNodeIsSource
                ? `${closestNode.node.id}-${node.id}`
                : `${node.id}-${closestNode.node.id}`,
            source: closeNodeIsSource ? closestNode.node.id : node.id,
            target: closeNodeIsSource ? node.id : closestNode.node.id,
        } as Edge;
    }

    const onDragOver = (event: React.DragEvent<HTMLDivElement>) => {
        event.preventDefault();
        event.dataTransfer.dropEffect = 'move';
    }

    const onDrop = (event: React.DragEvent<HTMLDivElement>) => {
        event.preventDefault();
        if (!type) {
            return;
        }
        const position = screenToFlowPosition({
            x: event.clientX,
            y: event.clientY,
        });
        const id = uuidv4()
        const newNode = {
            id: id,
            type: type,
            position,
            data: { label: `${type} node`, tool, orgTool, name: id, innerData: {} },
        } as AppNode;
        setNodes(nodes.concat(newNode));
    }

    const onNodeDragInner = (_event: React.MouseEvent, node: AppNode) => {
        const closestEdge = getClosestEdge(node);
        // Auto connect closer handler.
        onCloseNodeDrag(closestEdge);
    }

    const onNodeDragStopInner = (_event: React.MouseEvent, node: AppNode) => {
        const closestEdge = getClosestEdge(node);
        // Auto connect closer handler.
        onCloseNodeDragStop(closestEdge);
    }

    useEffect(() => {
        if (zoomCenter) {
            setCenter(zoomCenter.x, zoomCenter.y, {zoom: 3, duration: 1000})
        }
    }, [zoomCenter]);

    useEffect(() => {
        window.requestAnimationFrame(() => {
            fitView();
        });
    }, [relayOut]);

    return (
        <div className="w-full h-full flex flex">
            <div className="grow">
                <ReactFlow
                    nodes={nodes}
                    edges={edges}
                    edgeTypes={{
                        simpleEdge: SimpleEdge
                    }}
                    nodeTypes={{
                        "llm-model": LLM_ModelNode,
                        "llm-function": LLM_FunctionNode,
                    }}
                    fitView
                    fitViewOptions={{
                        padding: 0.2,
                    }}
                    defaultEdgeOptions={{
                        animated: true,
                    }}
                    zoomOnDoubleClick
                    onNodesChange={onNodesChange}
                    onEdgesChange={onEdgesChange}
                    onConnect={onConnect}
                    onNodeDrag={onNodeDragInner}
                    onNodeDragStop={onNodeDragStopInner}
                    onDrop={onDrop}
                    onDragOver={onDragOver}
                    isValidConnection={isValidConnection}
                >
                    <Controls/>
                    <MiniMap zoomable pannable position={"top-left"}/>
                    <Background variant={BackgroundVariant.Dots} color="#ddd" gap={24} size={2}/>
                    <DevTools/>
                </ReactFlow>
            </div>
            <div className="w-[200px] h-full border border-gray-100 bg-white hover:shadow hover:shadow-blue-50 ml-2 p-1 rounded">
                <ToolPanel />
            </div>
        </div>
    )
}