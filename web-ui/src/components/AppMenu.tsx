import {
    AppstoreOutlined,
    BoxPlotOutlined,
    CodeSandboxOutlined,
    ApiOutlined,
    RobotOutlined
} from '@ant-design/icons';
import type { MenuProps } from 'antd';
import { Menu } from "antd";
import {useLocation, useNavigate} from "react-router";
import { useLocalStorage } from "@uidotdev/usehooks";
import {useEffect} from "react";

type MenuItem = Required<MenuProps>['items'][number];

const items: MenuItem[] = [
    { key: '/dashboard/application', icon: <AppstoreOutlined />, label: '应用管理'},
    { key: '/dashboard/knowledgeBase', icon: <CodeSandboxOutlined />, label: '知识库管理' },
    { key: '/dashboard/workflow', icon: <BoxPlotOutlined />, label: '工作流管理' },
    {
        key: '/dashboard/task',
        label: '任务管理',
        icon: <RobotOutlined />,
        children: [
            { key: '/dashboard/task/knowledge', label: '知识库任务' },
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
                    { key: '/dashboard/tool/train-dataset', label: '数据预处理' },
                    { key: '/dashboard/tool/train-hyperParams', label: '训练参数以及策略' },
                    { key: '/dashboard/tool/train-post', label: '模型后处理' },
                    { key: '/dashboard/tool/train-upload', label: '模型上传/下载' },
                    { key: '/dashboard/tool/train-benchmark', label: '模型评测' },
                    { key: '/dashboard/tool/train-tokenizer', label: '数据向量化' },
                    { key: '/dashboard/tool/train-finetune', label: '模型微调' }
                ],
            },
        ],
    }
];

export default function AppMenu(){

    const navigate = useNavigate();
    // store menu state to local-storage.
    const [currentMenu, setCurrentMenu] =
        useLocalStorage("open-menu", '/dashboard/application')

    const navigateToMenu = (path: string) => {
        setParentMenu(path);
        navigate(path);
    }

    const setParentMenu = (path: string) => {
        if (path.startsWith("/dashboard/tool")) {
            setCurrentMenu("/dashboard/tool")
        } else if (path.startsWith("/dashboard/task")) {
            setCurrentMenu("/dashboard/task")
        } else {
            setCurrentMenu(path)
        }
    }

    const location = useLocation()

    useEffect(() => {
        setParentMenu(location.pathname)
    }, [location]);

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