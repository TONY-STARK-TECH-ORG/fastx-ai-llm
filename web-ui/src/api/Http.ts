import {Response} from "../store/define.ts";

export const api = (path: string) => {
    return "http://localhost:8082/" + path
}

class Http {

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
}

export const http = new Http();