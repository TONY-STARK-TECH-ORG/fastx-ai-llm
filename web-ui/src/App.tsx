import './App.css'
import {ConfigProvider, message} from 'antd';
import zhCN from 'antd/locale/zh_CN';
import en_US from 'antd/locale/en_US';
import { BrowserRouter, Routes, Route } from "react-router";
//----------------------------------------------------------------------
// Infos
//----------------------------------------------------------------------
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
//----------------------------------------------------------------------
// Dashboard pages: KnowledgeBase
//----------------------------------------------------------------------
import KnowledgeBasePage from "./pages/dashboard/KnowledgeBasePage.tsx";
//----------------------------------------------------------------------
// Dashboard pages: Workflow
//----------------------------------------------------------------------
import WorkflowPage from "./pages/dashboard/WorkflowPage.tsx";
//----------------------------------------------------------------------
// Dashboard pages: Tasks
//----------------------------------------------------------------------
import TaskPage from "./pages/dashboard/TaskPage.tsx";
//----------------------------------------------------------------------
// Dashboard pages: Tools
//----------------------------------------------------------------------
import ToolPage from "./pages/dashboard/ToolPage.tsx";
import LoadingPage from "./pages/loading/LoadingPage.tsx";
//----------------------------------------------------------------------
// Dashboard pages: Others
//----------------------------------------------------------------------

function App() {
    const locale = 'zhCN';
    // user localStorage store.
    const [userState, token] = useUserStore((state) => [state, state.token])

    // global loading.
    const [loading, setLoading] = useState(true)

    // global state.

    const [user, setUser] = useState<User | undefined>(undefined)
    const [organization, setOrganization] = useState<Organization[] | undefined>([])

    //----------------------------------------------------------------------
    // @TODO (stark) organization load need move to store.
    //----------------------------------------------------------------------
    const loadOrganizationList = async () => {
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
    }

    useEffect(() => {
        //----------------------------------------------------------------------
        // Avoid effect many times.
        //----------------------------------------------------------------------
        const fetchAppInitData = async () => {
            if (token) {
                setUser({...userState})
                await loadOrganizationList()
            }
            setTimeout(() => setLoading(false), 3000)
        }

        fetchAppInitData();
    }, [token])

    if (loading) {
        return <LoadingPage />
    }

    return (
        <ConfigProvider
            locale={locale === 'zhCN' ? zhCN : en_US}
            theme={{
                //----------------------------------------------------------------------
                // Ant design design tokens.
                //----------------------------------------------------------------------
                token: {
                    // Seed Token，影响范围大
                    colorPrimary: '#FF6A00',
                    borderRadius: 3,
                },
            }}
        >
            <div className="flex flex-col w-screen">
                <div className="grow">
                    <UserContext.Provider value={{user, setUser, organization, setOrganization}}>
                        <BrowserRouter>
                            <Routes>
                                <Route path={"/"} element={<HomePage/>}/>
                                <Route path={"/dashboard"} element={<DashboardPage/>}>
                                    <Route path="application" element={<ApplicationPage/>}/>
                                    <Route path="knowledgeBase" element={<KnowledgeBasePage/>}/>
                                    <Route path="tool/*" element={<ToolPage/>}/>
                                    <Route path="task/*" element={<TaskPage/>}/>
                                    <Route path="workflow" element={<WorkflowPage/>}/>
                                </Route>

                                <Route path={"/login"} element={<LoginPage/>}/>
                                <Route path={"/logout"} element={<LogoutPage/>}/>
                                <Route path={"/register"} element={<RegisterPage/>}/>
                            </Routes>
                        </BrowserRouter>
                    </UserContext.Provider>
                </div>
            </div>
        </ConfigProvider>
    )
}

export default App
