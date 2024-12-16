import {useUserStore} from "../store/UserStore.ts";
import AppMenu from "../components/AppMenu.tsx";
import {Layout, Avatar } from 'antd';
import {useEffect, useState} from "react";
import {useNavigate} from "react-router";
import { Outlet } from "react-router";
import OrganizationSelectMenu from "../components/OrganizationSelectMenu.tsx";
import UserSettings from "./setting/UserSettings.tsx";

const {Sider, Content} = Layout;

export default function DashboardPage () {
    const [isLogin] = useUserStore((state) => [state.isLogin])
    const [isModalOpen, setIsModalOpen] = useState(false);

    const navigate = useNavigate();

    const openUserinfoModel = () => {
        setIsModalOpen(true);
    }

    const handleCancel = () => {
        setIsModalOpen(false);
    }

    useEffect(() => {
        if (!isLogin()) {
            navigate("/login");
        }
    }, [isLogin()])

    return (
        <Layout className="w-screen h-screen">
            <Sider
                width={150}
                className="bg-white"
                collapsed
                collapsedWidth={50}
            >
                <div className="w-full h-full flex flex-col pb-2">
                    <OrganizationSelectMenu />
                    <div className="grow">
                        <AppMenu/>
                    </div>
                    <div className="w-full flex items-center justify-center py-2">
                        <Avatar onClick={openUserinfoModel} className="w-6 h-6 hover:cursor-pointer"
                                src="https://gw.alipayobjects.com/zos/rmsportal/KDpgvguMpGfqaHPjicRK.svg"/>
                        <UserSettings open={isModalOpen} onClose={handleCancel} />
                    </div>
                </div>
            </Sider>
            <Layout>
                <Content className="w-full h-screen">
                    <Outlet />
                </Content>
            </Layout>
        </Layout>
    )
}