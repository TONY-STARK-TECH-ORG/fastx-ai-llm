import {
    AppstoreOutlined,
    BoxPlotOutlined,
    CodeSandboxOutlined,
    ApiOutlined,
    RobotOutlined,
    FileExclamationOutlined
} from '@ant-design/icons';
import type { MenuProps } from 'antd';
import { Menu } from "antd";
import {useLocation, useNavigate} from "react-router";
import { useLocalStorage } from "@uidotdev/usehooks";

type MenuItem = Required<MenuProps>['items'][number];

const items: MenuItem[] = [
    { key: '/dashboard/application', icon: <AppstoreOutlined />, title: '应用管理'},
    { key: '/dashboard/knowledgeBase', icon: <CodeSandboxOutlined />, label: '知识库管理' },
    { key: '/dashboard/workflow', icon: <BoxPlotOutlined />, label: '工作流管理' },
    {
        key: '/dashboard/task',
        label: '任务管理',
        icon: <RobotOutlined />,
        children: [
            { key: '/dashboard/task/knowledgeBase', label: '知识库任务' },
            { key: '/dashboard/task/cron', label: '定时任务' },
            { key: '/dashboard/task/train', label: '模型训练任务' },
            { key: '/dashboard/task/custom', label: '自定义任务' },
        ],
    },
    {
        key: '/dashboard/tool',
        label: '工具管理',
        icon: <ApiOutlined />,
        children: [
            { key: '/dashboard/tool/llm-model', label: '大语言模型（LLMsProvider）' },
            { key: '/dashboard/tool/llm-tool', label: '大模型工具（FunctionCall）' },
            { key: '/dashboard/tool/llm-function', label: '输入输出预处理' },
            {
                key: '/dashboard/tool/train',
                label: '模型微调工具',
                children: [
                    { key: '/dashboard/tool/train/dataset', label: '数据预处理' },
                    { key: '/dashboard/tool/train/hyperParams', label: '训练参数以及策略' },
                    { key: '/dashboard/tool/train/post', label: '模型后处理' },
                    { key: '/dashboard/tool/train/upload', label: '模型上传/下载' },
                    { key: '/dashboard/tool/train/benchmark', label: '模型评测' },
                    { key: '/dashboard/tool/train/tokenizer', label: '数据向量化' },
                ],
            },
        ],
    },
    {
        key: '/dashboard/log',
        label: '日志查询',
        icon: <FileExclamationOutlined />,
        children: [
            { key: '/dashboard/log/workflow', label: '工作流执行日志' },
            { key: '/dashboard/log/task', label: '任务执行日志' },
        ],
    },
];

export default function AppMenu(){

    const navigate = useNavigate();
    // store menu state to local-storage.
    const [currentMenu, setCurrentMenu] =
        useLocalStorage("open-menu", '/dashboard/application')

    const navigateToMenu = (path: string) => {
        setCurrentMenu(path);
        navigate(path);
    }

    const location = useLocation()
    setCurrentMenu(location.pathname)

    return (
        <div style={{ width: 50 }}>
            <Menu
                defaultSelectedKeys={[currentMenu]}
                mode="inline"
                inlineCollapsed
                items={items}
                onSelect={(item) => {
                    navigateToMenu(item.key)
                }}
            />
        </div>
    );
};