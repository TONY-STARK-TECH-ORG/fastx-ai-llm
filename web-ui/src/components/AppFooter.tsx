import Footer from 'rc-footer';
import 'rc-footer/assets/index.css'; // import 'rc-footer/asssets/index.less';

function AppFooter(){
    return(
        <Footer
            columns={[
                {
                    icon: (
                        <img src="https://gw.alipayobjects.com/zos/rmsportal/XuVpGqBFxXplzvLjJBZB.svg" />
                    ),
                    title: '语雀',
                    items: [
                        {
                            title: '语雀首页',
                            url: 'https://www.yuque.com/',
                            openExternal: true,
                            description: '知识创作与分享工具',
                        },
                    ]
                },
                {
                    icon: (
                        <img src="https://gw.alipayobjects.com/zos/rmsportal/XuVpGqBFxXplzvLjJBZB.svg" />
                    ),
                    title: '语雀',
                    items: [
                        {
                            title: '语雀首页',
                            url: 'https://www.yuque.com/',
                            openExternal: true,
                            description: '知识创作与分享工具',
                        },
                    ]
                },
                {
                    icon: (
                        <img src="https://gw.alipayobjects.com/zos/rmsportal/XuVpGqBFxXplzvLjJBZB.svg" />
                    ),
                    title: '语雀',
                    items: [
                        {
                            title: '语雀首页',
                            url: 'https://www.yuque.com/',
                            openExternal: true,
                            description: '知识创作与分享工具',
                        },
                    ]
                },
                {
                    icon: (
                        <img src="https://gw.alipayobjects.com/zos/rmsportal/XuVpGqBFxXplzvLjJBZB.svg" />
                    ),
                    title: '语雀',
                    items: [
                        {
                            title: '语雀首页',
                            url: 'https://www.yuque.com/',
                            openExternal: true,
                            description: '知识创作与分享工具',
                        },
                    ]
                },
            ]}
            bottom="Made with ❤️ by fastx-ai.com"
        />
    );
}

export default AppFooter;