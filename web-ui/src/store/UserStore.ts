import { createWithEqualityFn } from 'zustand/traditional'
import {createJSONStorage, devtools} from 'zustand/middleware'
import {persist} from 'zustand/middleware'
import {UserAction, User} from "./user/User.ts";
import {http} from "../api/Http.ts";
import md5 from "md5";
import type {StateCreator} from "zustand/vanilla";

// combine user type and user actions
export type UserStore = User & UserAction;

// create store
const userStore: StateCreator<UserStore> = (set, get) => ({
    // user state
    id: "",
    authProvider: "",
    bio: "",
    email: "",
    expertiseAreas: "",
    lastLogin: "",
    preferredLanguage: "",
    profileImageUrl: "",
    role: "",
    socialLinks: "",
    status: "",
    username: "",
    token: "",
    createTime: "",
    updateTime: "",
    password: "",
    // user actions
    createUser: async (email: string, password: string) => {
        // call API to create user
        const resp = await http.post<User>('auth/user/createWithEmail', {
            email: email,
            password: md5(password),
        });
        if (resp.success) {
            set({
                ...resp.data
            })
        }
        return resp;
    },
    login: async (email: string, password: string) => {
        // call API to create user
        const resp = await http.post<User>('auth/user/loginWithEmail', {
            email: email,
            password: md5(password),
        });
        if (resp.success) {
            set({
                ...resp.data
            })
        }
        return resp;
    },
    logout: () => {
        set({
            id: "",
            authProvider: "",
            bio: "",
            email: "",
            expertiseAreas: "",
            lastLogin: "",
            preferredLanguage: "",
            profileImageUrl: "",
            role: "",
            socialLinks: "",
            status: "",
            username: "",
            token: "",
            createTime: "",
            updateTime: "",
        })
    },
    isLogin: () => {
        return !!get().token;
    }
});

const persistentOptions = {
    name: "user-storage",
    storage: createJSONStorage(() => localStorage)
}
// export user store
export const useUserStore = createWithEqualityFn<UserStore>()(
    devtools(persist(userStore, persistentOptions))
)