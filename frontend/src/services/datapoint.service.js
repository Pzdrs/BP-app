import api from "@/axios";

export default {
    listen: (dataSources) => {
        const params = new URLSearchParams({
            sources: dataSources.join(',')
        })
        return new EventSource(`${api.defaults.baseURL}/datapoint/listen?${params}`, {withCredentials: true});
    },
    getMonthlyBreakdown: () => {
        return api.get(`/datapoint/breakdown`);
    }
}
