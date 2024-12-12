import { Button } from 'antd';
import {useUserStore} from "../store/UserStore.ts";
import {useNavigate} from "react-router";

export default function HomePage() {
    const [isLogin, logout] = useUserStore((state) => [state.isLogin, state.logout])
    const navigate = useNavigate();

    return (
        <div className="flex flex-col items-center justify-center bg-black w-full h-full">
            {isLogin() && <Button type="primary" onClick={()=>{ navigate("/dashboard") }}>已登录，去仪表盘</Button>}
            {isLogin() && <Button className="mt-2" onClick={logout}>退出</Button>}
            {!isLogin() && <Button className="mt-2" onClick={()=>{ navigate("/login") }}>去登录</Button>}
        </div>
    )
}