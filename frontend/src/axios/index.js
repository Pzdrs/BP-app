import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080',
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
