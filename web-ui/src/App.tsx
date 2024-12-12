import './App.css'
import { ConfigProvider } from 'antd';
import zhCN from 'antd/locale/zh_CN';
import en_US from 'antd/locale/en_US';
import AppFooter from "./components/AppFooter.tsx";
import { BrowserRouter, Routes, Route } from "react-router";
import HomePage from "./pages/HomePage.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import DashboardPage from "./pages/DashboardPage.tsx";

function App() {
    const locale = 'zhCN';
    return (
        <ConfigProvider
            locale={locale === 'zhCN' ? zhCN : en_US}
            theme={{
                token: {
                    // Seed Token，影响范围大
                    colorPrimary: '#FF6A00',
                    borderRadius: 2,
                },
            }}
        >
            <div className="w-screen h-screen flex flex-col">
                <div className="grow">
                    <BrowserRouter>
                        <Routes>
                            <Route path={"/"} element={<HomePage/>}/>
                            <Route path={"/login"} element={<LoginPage/>}/>
                            <Route path={"/dashboard"} element={<DashboardPage/>}/>
                        </Routes>
                    </BrowserRouter>
                </div>
            </div>
            <AppFooter/>
        </ConfigProvider>
    )
}

export default App
