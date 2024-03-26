import axios from "@/axios";
import api from "@/axios";

export default {
    getAll: async () => {
        const res = await axios.get('/notification');
        return res?.data ?? [];
    },
    listen: () => {
        return new EventSource(`${api.defaults.baseURL}/notification/live`, {withCredentials: true});
    },
    dismiss: async (id) => {
        return await axios.delete(`/notification/${id}`);
    }
}
