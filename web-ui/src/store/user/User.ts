import {Base, Response, Token} from "../define.ts";

export interface User extends Base, Token {
    username: string;
    email: string;
    password: string;
    role: string;
    status: string;
    expertiseAreas: string;
    lastLogin: string;
    preferredLanguage: string;
    profileImageUrl: string;
    socialLinks: string;
    authProvider: string;
    bio: string;
}

export interface UserAction {
    createUser: (email: string, password: string) => Promise<Response<User>>;

    login: (email: string, password: string) => Promise<Response<User>>;
    logout: () => void;

    isLogin: () => boolean;
}