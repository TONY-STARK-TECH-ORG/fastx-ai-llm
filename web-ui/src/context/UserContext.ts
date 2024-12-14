import React from "react";
import { User } from "../store/user/User"
import {Organization} from "../store/define.ts";

export type UserContextType = {
    user: User | undefined;
    setUser: (user: User) => void;
    organization: Organization[] | undefined;
    setOrganization: (organizations: Organization[]) => void;
}

export const UserContext = React.createContext<UserContextType>({
    user: {
        username: "",
        email: "",
        password: "",
        role: "",
        status: "",
        expertiseAreas: "",
        lastLogin: "",
        preferredLanguage: "",
        profileImageUrl: "",
        socialLinks: "",
        authProvider: "",
        bio: "",
        createTime: "",
        id: "",
        updateTime: "",
        token: ""
    },
    setUser: () => {
        // ignored
    },
    organization: [{
        createTime: "",
        id: "",
        updateTime: "",
        name: ""
    }],
    setOrganization: () => {
        // ignored
    }
})