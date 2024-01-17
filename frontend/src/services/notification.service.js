import axios from "@/axios";

export default {
    getAll: async () => {
        const res = await axios.get('/notification');
        return res?.data ?? [];
    },
    listen: () => {
        const eventSource = new EventSource('/notification/live');
        eventSource.addEventListener('message', (e) => {
            console.log(e)
        });
        return eventSource;
    },
    dismiss: async (id) => {
        return await axios.delete(`/notification/${id}`);
    }
}
