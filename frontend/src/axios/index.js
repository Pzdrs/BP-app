import axios from "axios";

const api = axios.create({
    // has to be supplied at build time, not runtime
    baseURL: import.meta.env.VITE_BACKEND_URL,
    xsrfCookieName: null,
    xsrfHeaderName: null,
    withCredentials: true,
});

export function post(url, data) {
    api.get("/auth/csrf").then(r => {
        return api.post(url, data, {
            headers: {
                'X-XSRF-TOKEN': r.data.token
            }
        });
    })
}

export default api;
