export default {
    listen: (dataSources) => {
        const params = new URLSearchParams({
            sources: dataSources.join(',')
        })
        return new EventSource(`http://localhost:8080/datapoint/listen?${params}`, {withCredentials: true});
    },
}
