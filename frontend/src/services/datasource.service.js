import axios from "@/axios";

export default {
    getAll: async () => {
        const res = await axios.get('/datasource/all');
        return res?.data ?? [];
    },
    getGroups: async () => {
        const res = await axios.get('/datasource/groups');
        return res?.data ?? [];
    },
    update: async (id, data) => {
        return await axios.patch(`/datasource/${id}`, data);
    },
    delete: async (id) => {
        return await axios.delete(`/datasource/${id}`);
    },
    adopt: async (id, data) => {
        return await axios.post(`/datasource/${id}/adopt`, data);
    }
}
