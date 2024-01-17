import axios from "@/axios";

export default {
    getAll: async () => {
        const res = await axios.get('/user/all');
        return res?.data ?? [];
    },
    getRoles: async () => {
        const res = await axios.get('/user/roles');
        return res?.data ?? [];
    },
    create: async (data) => {
        return await axios.post('/user/register', data);
    },
    update: async (id, data) => {
        return await axios.patch(`/user/${id}`, data);
    },
    delete: async (id) => {
        return await axios.delete(`/user/${id}`);
    }
}
