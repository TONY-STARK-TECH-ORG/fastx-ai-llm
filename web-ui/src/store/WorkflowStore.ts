import {createWithEqualityFn} from 'zustand/traditional'
import {
    addEdge,
    applyEdgeChanges,
    applyNodeChanges,
    getOutgoers,
    type Edge, IsValidConnection,
    type Node,
    type OnConnect,
    type OnEdgesChange,
    type OnNodesChange,
} from '@xyflow/react';
import Dagre from "@dagrejs/dagre";

export type AppNode = Node & {};

export type AppState = {
    nodes: AppNode[];
    edges: Edge[];
    onNodesChange: OnNodesChange<AppNode>;
    onEdgesChange: OnEdgesChange;

    onConnect: OnConnect;
    isValidConnection: IsValidConnection;

    setNodes: (nodes: AppNode[]) => void;
    setEdges: (edges: Edge[]) => void;

    reLayout: number;
    onLayout: () => void;

    zoomCenter: {x: number, y: number} | undefined;
    onZoomSelected: () => void;

    getLayoutedElements: (nodes: AppNode[], edges: Edge[], options: any) => {nodes: AppNode[]; edges: Edge[]};

    onCloseNodeDragStop: (closeEdge: Edge | undefined) => void;
    onCloseNodeDrag: (closeEdge: Edge | undefined) => void;

    onSave: () => { nodes: AppNode[], edges: Edge[] };
    onInit: (jsonData: string) => boolean;

    executeResult: any;
    setExecuteResult: (result: any) => void;
};

const useWorkflowStore = createWithEqualityFn<AppState>((set, get) => ({
    nodes: [],
    edges: [],
    reLayout: 0,
    zoomCenter: undefined,
    executeResult: {},
    setExecuteResult: (result) => {
        set({
            executeResult: result ?? {}
        })
        // set execute result to nodes.
        for (const k in result.outputs) {
            const value = result.outputs[k];
            get().nodes.forEach(node => {
                if (node.data.name == k) {
                    node.data.executeResult = value;
                }
            })
        }
    },
    onNodesChange: (changes) => {
        set({
            nodes: applyNodeChanges(changes, get().nodes),
        });
    },
    onEdgesChange: (changes) => {
        set({
            edges: applyEdgeChanges(changes, get().edges),
        });
    },
    onConnect: (connection) => {
        const edge = { ...connection, type: 'simpleEdge' };
        set({
            edges: addEdge(edge, get().edges),
        });
    },
    isValidConnection: (connection) => {
        const nodes = get().nodes;
        const edges = get().edges;
        const target = nodes.find((node) => node.id === connection.target);
        const hasCycle = (node: any, visited = new Set()) => {
            if (visited.has(node.id)) return false;

            visited.add(node.id);

            for (const outgoer of getOutgoers(node, nodes, edges)) {
                if (outgoer.id === connection.source) return true;
                if (hasCycle(outgoer, visited)) return true;
            }
        };

        if (target?.id === connection.source) return false;
        return !hasCycle(target);
    },
    setNodes: (nodes) => {
        set({ nodes });
    },
    setEdges: (edges) => {
        set({ edges });
    },
    onLayout: () => {
        // layout.
        const layouted = get().getLayoutedElements(get().nodes, get().edges, { direction: 'LR' });

        get().setNodes([...layouted.nodes]);
        get().setEdges([...layouted.edges]);

        set({ reLayout: get().reLayout + 1 });
    },
    onZoomSelected: () => {
        const node = get().nodes.find(n => n.selected);
        if (node) {
            set({ zoomCenter: node.position });
        } else {
            set({ zoomCenter: undefined });
        }
    },
    getLayoutedElements: (nodes, edges, options) => {
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
                // We are shifting the dagre node position (anchor=center-center) to the top left
                // so it matches the React Flow node anchor point (top left).
                const x = position.x - (node.measured?.width ?? 0) / 2;
                const y = position.y - (node.measured?.height ?? 0) / 2;

                return { ...node, position: { x, y } };
            }),
            edges,
        };
    },
    onCloseNodeDragStop: (closeEdge) => {
        const edges = (es: Edge[]) => {
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
        }
        get().setEdges(edges(get().edges));
    },
    onCloseNodeDrag: (closeEdge) => {
        const edges = (es: Edge[]) => {
            const nextEdges = es.filter((e) => e.className !== 'temp');
            if (
                closeEdge &&
                !nextEdges.find(
                    (ne: Edge) =>
                        ne.source === closeEdge.source && ne.target === closeEdge.target,
                )
            ) {
                closeEdge.className = 'temp';
                nextEdges.push(closeEdge);
            }
            return nextEdges;
        }

        get().setEdges(edges(get().edges));
    },
    onSave: () => {
        get().onLayout()

        // clear executeResult.
        get().nodes.forEach(node => {
            node.data.executeResult = -1;
        })

        return  {
            nodes: get().nodes || [],
            edges: get().edges || [],
        }
    },
    onInit: (jsonData) => {
        const object = JSON.parse(jsonData);
        get().setNodes(object.nodes || []);
        get().setEdges(object.edges || []);
        get().onLayout();
        return true
    }
}));

export default useWorkflowStore;