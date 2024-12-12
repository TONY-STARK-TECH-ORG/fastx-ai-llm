import { useForm } from "react-hook-form"
import {useUserStore} from "../store/UserStore.ts";
import {message} from "antd";
import {useNavigate} from "react-router";

export default function LoginPage() {
    const { register, handleSubmit, formState: { errors } } = useForm()
    const [login] = useUserStore((state) => [state.login])
    const navigate = useNavigate()

    return (
        <div className="flex flex-col items-center justify-center w-screen h-screen">
            <h1 className="text-3xl font-bold mb-4">Login</h1>
            <form onSubmit={handleSubmit(async (data) => {
                const resp = await login(data.email, data.password)
                if (!resp.success) {
                    message.error("登录失败：" + resp.msg)
                } else {
                    message.success("登录成功！")
                    navigate("/dashboard")
                }
            })}>
                <div className="mb-4">
                    <label htmlFor="email" className="block text-gray-700 font-bold mb-2">Email</label>
                    <input type="email" id="email" {...register("email", {required: true})} className="shadow appearance-none" />
                    {errors.email && <p>Email is required.</p>}
                </div>
                <div className="mb-4">
                    <label htmlFor="password" className="block text-gray-700 font-bold mb-2">Password</label>
                    <input type="password" id="password" {...register("password")} className="shadow appearance-none" />
                </div>
                <button type="submit" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Login</button>
            </form>
        </div>
    )
}