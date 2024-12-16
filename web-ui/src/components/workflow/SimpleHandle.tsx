import {Handle, HandleProps, useHandleConnections} from '@xyflow/react';

export type SimpleHandleProps = HandleProps & {
    connectionCount: number;
}

export default function SimpleHandle (props: SimpleHandleProps){
    const connections = useHandleConnections({
        type: props.type,
    });

    return (
        <Handle
            {...props}
            isConnectable={connections.length < props.connectionCount}
        />
    );
};