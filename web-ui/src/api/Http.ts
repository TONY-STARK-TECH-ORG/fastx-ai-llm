import {Response} from "../store/define.ts";

export const api = (path: string) => {
    return "http://localhost:8082/" + path
}

class Http {

    async getWithParams<T>(path: string, params: any) {
        return this.get<T>(path + "?" + new URLSearchParams(params).toString())
    }

    async get<T>(path: string) {
        const apiURL = api(path);
        const res = await fetch(apiURL, {
            method: 'GET',
            headers: this.headers()
        });
        if (res.ok) {
            const response = await res.json() as Response<T>
            // business success
            if (response.code === 200) {
                return {
                    ...response,
                    success: true,
                }
            } else {
                return {
                    ...response,
                    success: false,
                }
            }
        }
        return {
            code: 400,
            data: undefined,
            success: false,
            msg: 'unknown network error'
        }
    }

    async post<T>(path: string, body: any) {
        const apiURL = api(path);
        const res = await fetch(apiURL, {
            method: 'POST',
            headers: this.headers(),
            body: JSON.stringify(body)
        });
        if (res.ok) {
            const response = await res.json() as Response<T>
            // business success
            if (response.code === 200) {
                return {
                    ...response,
                    success: true,
                }
            } else {
                return {
                    ...response,
                    success: false,
                }
            }
        }
        return {
            code: 400,
            data: undefined,
            success: false,
            msg: 'unknown network error'
        }
    }

    async stream(path: string, body: any, onData: (value: string | undefined, stop: boolean | undefined)=>void) {
        const apiURL = api(path);
        const res = await fetch(apiURL, {
            method: 'POST',
            headers: this.headers(),
            body: JSON.stringify(body)
        });
        const reader = res?.body?.getReader();
        while (true) {
            const r = await reader?.read();
            if (r?.done) {
                onData(new TextDecoder().decode(r?.value), true)
                break;
            }
            onData(new TextDecoder().decode(r?.value), false)
        }
        return res;
    }

    headers() {
        const userStorage = localStorage.getItem("user-storage")
        if (!userStorage) {
            return {
                'Content-Type': 'application/json',
                'Authorization': ''
            }
        }

        const userState = JSON.parse(userStorage);
        return {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + userState.state.token!!
        }
    }

    uploadHeaders() {
        const userStorage = localStorage.getItem("user-storage")
        if (!userStorage) {
            return {
                'Authorization': ''
            }
        }

        const userState = JSON.parse(userStorage);
        return {
            'Authorization': 'Bearer ' + userState.state.token!!
        }
    }
}

export const http = new Http();