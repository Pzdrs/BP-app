import axios from "@/axios";

export default {
    getAll: async () => {
        const res = await axios.get('/token/all');
        return res?.data ?? [];
    },
    issue: async (data) => {
        return await axios.post('/token/issue', data);
    },
    update: async (id, data) => {
        return await axios.patch(`/token/${id}`, data);
    },
    delete: async (id) => {
        return await axios.delete(`/token/${id}`);
    }
}
