import './App.css'
import {ConfigProvider, message, Spin} from 'antd';
import zhCN from 'antd/locale/zh_CN';
import en_US from 'antd/locale/en_US';
import { BrowserRouter, Routes, Route } from "react-router";

import {UserContext} from "./context/UserContext.ts";
import {useEffect, useState} from "react";
import {User} from "./store/user/User.ts";
import {useUserStore} from "./store/UserStore.ts";

//----------------------------------------------------------------------
// Main pages
//----------------------------------------------------------------------
import HomePage from "./pages/HomePage.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import DashboardPage from "./pages/DashboardPage.tsx";
import RegisterPage from "./pages/RegisterPage.tsx";
import LogoutPage from "./pages/LogoutPage.tsx";
//----------------------------------------------------------------------
// Dashboard pages: Application
//----------------------------------------------------------------------
import ApplicationPage from "./pages/dashboard/ApplicationPage.tsx";
import {http} from "./api/Http.ts";
import {Organization} from "./store/define.ts";
import KnowledgeBasePage from "./pages/dashboard/KnowledgeBasePage.tsx";
import ToolPage from "./pages/dashboard/ToolPage.tsx";
import TaskPage from "./pages/dashboard/TaskPage.tsx";
import LogPage from "./pages/dashboard/LogPage.tsx";
//----------------------------------------------------------------------
// Dashboard pages: KnowledgeBase
//----------------------------------------------------------------------

//----------------------------------------------------------------------
// Dashboard pages: Workflow
//----------------------------------------------------------------------

//----------------------------------------------------------------------
// Dashboard pages: Tasks
//----------------------------------------------------------------------

//----------------------------------------------------------------------
// Dashboard pages: Tools
//----------------------------------------------------------------------

//----------------------------------------------------------------------
// Dashboard pages: Logs
//----------------------------------------------------------------------

//----------------------------------------------------------------------
// Dashboard pages: Others
//----------------------------------------------------------------------

function App() {
    const locale = 'zhCN';
    const [user, setUser] = useState<User | undefined>(undefined)
    const [userState, token] = useUserStore((state) => [state, state.token])
    const [loading, setLoading] = useState(false)

    const [organization, setOrganization] = useState<Organization[] | undefined>([])

    const loadOrganizationList = async () => {
        setLoading(true);
        if (userState === undefined) {
            return
        }
        const res =
            await http.get<Organization[]>("auth/user/getOrganizationsByUserId?userId=" + userState.id)
        if (res.success) {
            setOrganization(res.data)
        } else {
            message.error("所属组织加载失败，请重新打开创建窗口并重试")
        }
        setLoading(false);
    }

    useEffect(() => {
        setUser({...userState})
        loadOrganizationList()
    }, [token])

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
            <Spin spinning={loading}>
                <div className="flex flex-col w-screen">
                    <div className="grow">
                        <UserContext.Provider value={{user, setUser, organization, setOrganization}}>
                            <BrowserRouter>
                                <Routes>
                                    <Route path={"/"} element={<HomePage/>}/>
                                    <Route path={"/dashboard"} element={<DashboardPage/>}>
                                        <Route path="application" element={<ApplicationPage/>}/>
                                        <Route path="knowledgeBase" element={<KnowledgeBasePage />}/>
                                        <Route path="tool/*" element={<ToolPage />} />
                                        <Route path="task/*" element={<TaskPage />} />
                                        <Route path="log/*" element={<LogPage />} />
                                    </Route>

                                    <Route path={"/login"} element={<LoginPage/>}/>
                                    <Route path={"/logout"} element={<LogoutPage/>}/>
                                    <Route path={"/register"} element={<RegisterPage/>}/>
                                </Routes>
                            </BrowserRouter>
                        </UserContext.Provider>
                    </div>
                </div>
            </Spin>
        </ConfigProvider>
    )
}

export default App
