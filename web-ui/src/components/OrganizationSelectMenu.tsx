import {
    TeamOutlined, UserSwitchOutlined,
} from '@ant-design/icons';
import {MenuProps, message} from 'antd';
import { Menu } from "antd";
import {useContext, useEffect, useState} from "react";
import {UserContext} from "../context/UserContext.ts";
import {useOrganizationStore} from "../store/OrganizationStore.ts";

type MenuItem = Required<MenuProps>['items'][number];

export default function OrganizationSelectMenu(){

    const [menuItems, setMenuItems] = useState<MenuItem[] | undefined>([])
    const [currentOrganizationId, selectOrganization] =
        useOrganizationStore(state => [state.id, state.selectOrganization])

    const { organization } = useContext(UserContext)

    const resetMenuItems = () => {
        const items: MenuItem[] | undefined =
            organization?.map(o => ({'key': o.id!!, icon: <TeamOutlined />, 'label': "组织：" + o.name}))
        setMenuItems([
            {
                key: '-',
                icon: <UserSwitchOutlined />,
                children: items
            },
        ])
        // reset to select menu.
    }

    useEffect(() => {
        resetMenuItems()
    }, [organization]);

    return (
        <div style={{ width: 50 }}>
            <Menu
                defaultSelectedKeys={currentOrganizationId ? [currentOrganizationId + "", "-"]: ["-"]}
                selectedKeys={currentOrganizationId ? [currentOrganizationId + "", "-"]: ["-"]}
                mode="inline"
                inlineCollapsed
                items={menuItems}
                onSelect={(item) => {
                    const selectId = item.key;
                    const selectOrg = organization?.find(o => o.id == selectId)
                    if (!selectOrg) {
                        message.error("本地组织数据有误，请重新刷新界面")
                        return
                    }
                    selectOrganization(selectOrg)
                    location.reload()
                }}
            />
        </div>
    );
};