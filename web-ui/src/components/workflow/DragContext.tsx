import { createContext, useContext, useState } from 'react';

export type ContextType = {
    type: string | undefined;
    setType: (type: string | undefined) => void;
}

const DragContext = createContext<ContextType>({
    type: undefined,
    setType: () => {}
});

export const DragProvider = ({ children }: {children: any}) => {
    const [type, setType] = useState<string | undefined>(undefined);

    return (
        <DragContext.Provider value={{type, setType}}>
            {children}
        </DragContext.Provider>
);
}

export default DragContext;

export const useDrag = () => {
    return useContext(DragContext);
}