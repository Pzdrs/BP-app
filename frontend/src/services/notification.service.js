import axios from "@/axios";

export default {
    getAll: async () => {
        const res = await axios.get('/notification');
        return res?.data ?? [];
    },
    listen: () => {
        return new EventSource('http://localhost:8080/notification/live', {withCredentials: true});
    },
    dismiss: async (id) => {
        return await axios.delete(`/notification/${id}`);
    }
}
