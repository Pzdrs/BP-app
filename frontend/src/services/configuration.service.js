import axios from "@/axios";

export default {
    getMqtt: async () => {
        const res = await axios.get('/config/mqtt');
        return res?.data ?? {};
    },
    updateMqtt: async (data) => {
        return await axios.post('/config/mqtt', data);
    }
}
