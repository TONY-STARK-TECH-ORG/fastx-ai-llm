import {Spin} from "antd";

export default function LoadingPage () {
    return (
        <div className="w-screen h-screen flex items-center justify-center">
            <div>
                <Spin spinning={true} size={'large'}/>
            </div>
        </div>
    )
}