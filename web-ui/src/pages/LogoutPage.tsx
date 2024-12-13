import {useUserStore} from "../store/UserStore.ts";
import {useNavigate} from "react-router";
import {useEffect} from "react";

export default function LogoutPage() {
    const [logout] = useUserStore((state) => [state.logout])
    const navigate = useNavigate()

    useEffect(() => {
        setTimeout(()=> {
            logout()
            navigate("/")
        }, 3000);
    }, [])

    return (
        <>
            <span className="text-xs">退出登录中...</span>
        </>
    );
}