import {useUserStore} from "../store/UserStore.ts";
import {useNavigate} from "react-router";
import {useEffect} from "react";
import {useOrganizationStore} from "../store/OrganizationStore.ts";

export default function LogoutPage() {
    const [logout] = useUserStore((state) => [state.logout])
    const [setOrganization] = useOrganizationStore((state) => [state.selectOrganization])
    const navigate = useNavigate()

    useEffect(() => {
        setTimeout(()=> {
            logout()
            setOrganization(undefined)
            localStorage.clear()
            navigate("/")
        }, 3000);
    }, [])

    return (
        <>
            <span className="text-xs">退出登录中...</span>
        </>
    );
}