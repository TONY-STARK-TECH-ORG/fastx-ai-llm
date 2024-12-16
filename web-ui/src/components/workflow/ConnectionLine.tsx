import {ConnectionLineComponentProps, useConnection} from '@xyflow/react';

export default function ConnectionLine({ fromX, fromY, toX, toY }: ConnectionLineComponentProps){
    const { fromHandle } = useConnection();

    const getColor = () => {
        if (fromHandle?.id === 'red') {
            return 'red';
        }
        return '#000'
    }

    return (
        <g>
            <path
                fill="none"
                stroke={getColor()}
                strokeWidth={1.5}
                className="animated"
                d={`M${fromX},${fromY} C ${fromX} ${toY} ${fromX} ${toY} ${toX},${toY}`}
            />
            <circle
                cx={toX}
                cy={toY}
                fill="#fff"
                r={3}
                stroke={getColor()}
                strokeWidth={1.5}
            />
        </g>
    );
};