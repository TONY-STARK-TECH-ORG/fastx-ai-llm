import './App.css'
import { ConfigProvider } from 'antd';
import zhCN from 'antd/locale/zh_CN';
import en_US from 'antd/locale/en_US';
import { BrowserRouter, Routes, Route } from "react-router";
import HomePage from "./pages/HomePage.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import DashboardPage from "./pages/DashboardPage.tsx";
import RegisterPage from "./pages/RegisterPage.tsx";
import LogoutPage from "./pages/LogoutPage.tsx";

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
            <div className="flex flex-col">
                <div className="grow">
                    <BrowserRouter>
                        <Routes>
                            <Route path={"/"} element={<HomePage />}/>
                            <Route path={"/dashboard"} element={<DashboardPage />}/>

                            <Route path={"/login"} element={<LoginPage />}/>
                            <Route path={"/logout"} element={<LogoutPage />}/>
                            <Route path={"/register"} element={<RegisterPage />}/>
                        </Routes>
                    </BrowserRouter>
                </div>
            </div>
        </ConfigProvider>
    )
}

export default App
