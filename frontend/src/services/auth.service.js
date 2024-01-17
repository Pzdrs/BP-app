import axios from "@/axios";

export default {
    login: async (username, password) => {
        return await axios.post('/auth/login', {username, password});
    },
    logout: async () => {
        return await axios.post('/auth/logout');
    },
    getProfile: async () => {
        const res = await axios.get('/user/me').catch(() => null);
        return res?.data ?? null;
    }
}
