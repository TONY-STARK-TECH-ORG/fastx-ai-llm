import { createContext, useContext, useState } from 'react';
import {Tool} from "../../store/tool/Tool.ts";
import {OrgTool} from "../../store/define.ts";

export type ContextType = {
    type: string | undefined;
    setType: (type: string | undefined) => void;

    tool: Tool | undefined;
    setTool: (tool: Tool | undefined) => void;

    orgTool: OrgTool | undefined;
    setOrgTool: (orgTool: OrgTool | undefined) => void;
}

const DragContext = createContext<ContextType>({
    type: undefined,
    setType: () => {},

    tool: undefined,
    setTool: (_tool) => {},

    orgTool: undefined,
    setOrgTool: (_orgTool) => {}
});

export const DragProvider = ({ children }: {children: any}) => {
    const [type, setType] = useState<string | undefined>(undefined);
    const [tool, setTool] = useState<Tool | undefined>(undefined);
    const [orgTool, setOrgTool] = useState<OrgTool | undefined>(undefined);

    return (
        <DragContext.Provider value={{type, setType, tool, setTool, orgTool, setOrgTool}}>
            {children}
        </DragContext.Provider>
);
}

export default DragContext;

export const useDrag = () => {
    return useContext(DragContext);
}