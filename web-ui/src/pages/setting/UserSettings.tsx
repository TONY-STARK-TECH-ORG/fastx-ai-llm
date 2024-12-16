import {Menu, Modal, type MenuProps, Button, Avatar} from "antd";
import {useLocalStorage} from "@uidotdev/usehooks";
import {useNavigate} from "react-router";
import {useUserStore} from "../../store/UserStore.ts";
import {useContext} from "react";
import {UserContext} from "../../context/UserContext.ts";

type MenuItem = Required<MenuProps>['items'][number];

const items: MenuItem[] = [
    { key: '/setting/basic', label: '基本设置'},
    { key: '/setting/user', label: '用户设置' },
    { key: '/setting/org', label: '组织管理' },
];

export default function UserSettings({open, onClose}:{open: boolean; onClose:()=>void;}) {

    const [currentMenu, setCurrentMenu] =
        useLocalStorage("open-setting-menu", '/setting/basic')
    const navigate = useNavigate();

    const [userState] = useUserStore(state => [state])
    const { organization } = useContext(UserContext)

    const navigateToMenu = (key: string | undefined) => {
        setCurrentMenu(key!!)
    }

    const getContent = () => {
        if (currentMenu === '/setting/basic') {
            return <p className="font-medium text-xs">暂无基础设置</p>
        } else if (currentMenu === '/setting/user') {
            return (
                <div className="w-full h-full flex flex-col">
                    <p className="font-medium text-xs">基本信息：</p>
                    <Avatar className="border border-gray-100 mt-2" shape="square"
                            src={userState.profileImageUrl && userState.profileImageUrl.length != 0 ? userState.profileImageUrl : "https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png"}/>
                    <p className="font-medium text-xs mt-1">邮箱：<span className="font-thin">{userState.email}</span>
                    </p>
                    <p className="font-medium text-xs mt-1">用户昵称：<span
                        className="font-thin">{userState.username ?? "未设置"}</span></p>
                    <p className="font-medium text-xs mt-1">用户角色：<span
                        className="font-thin">{userState.role === 'normal' ? '普通用户' : '管理员'}</span></p>
                    <p className="font-medium text-xs mt-1">用户状态：<span
                        className="font-thin">{userState.status === 'wait' ? '待激活' : (userState.status === 'active' ? '激活' : '禁用')}</span>
                    </p>
                    <p className="font-medium text-xs mt-1">用户创建时间：<span
                        className="font-thin">{userState.createTime}</span></p>
                    <p className="font-medium text-xs mt-1">用户更新时间：<span
                        className="font-thin">{userState.updateTime}</span></p>
                    <div className="grow"></div>
                    <Button type="primary" size="small" onClick={() => {
                        navigate("/logout")
                    }}>退出登录</Button>
                </div>
            )
        } else if (currentMenu === '/setting/org') {
            return (
                <div className="w-full h-full">
                    <p className="font-medium text-xs">当前用户拥有的组织：</p>
                    {organization?.map(item => (
                        <p className="font-thin" key={item.id}>{item.name}</p>
                    ))}
                </div>
            )
        }

        return <p>暂无</p>
    }

    const getChildren = () => {
        return (
            <div className="w-full h-full bg-[#fafafa] rounded py-1 pr-1">
                <div className="w-full h-full bg-white rounded p-2">
                    {getContent()}
                </div>
            </div>
        )
    }

    return (
        <Modal
            title={null}
            open={open}
            onOk={async () => {
                // 1.0 validated content.
            }}
            onCancel={onClose}
            closeIcon={null}
            footer={null}
            className="!w-[500px]"
            styles={{content: {padding: 0}}}
        >
            <div className="h-[300px] bg-white rounded flex">
                <div className="h-full bg-[#fafafa] p-1 rounded">
                    <Menu
                        mode="vertical"
                        className="h-full rounded"
                        defaultSelectedKeys={[currentMenu]}
                        inlineCollapsed={false}
                        items={items}
                        onSelect={(item) => {
                            navigateToMenu(item.key)
                        }}
                    />
                </div>
                <div className="grow h-full w-full">
                    {getChildren()}
                </div>
            </div>
        </Modal>
    )
}