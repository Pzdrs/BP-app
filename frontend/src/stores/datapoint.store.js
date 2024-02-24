import {defineStore} from 'pinia'
import datasourceService from "@/services/datasource.service";
import axios from "@/axios";
import notificationService from "@/services/notification.service";
import datapointService from "@/services/datapoint.service";

export const useDataPointStore = defineStore('data_point', {
    state: () => ({
        dataPoints: [],
        dataSources: []
    }),
    getters: {
        getLatLngs: (state) => {
            return state.dataPoints.map(point => {
                return [point.lat, point.lng]
            })
        }
    },
    actions: {
        fetchDataPoints(dataSource, startDate, endDate) {
            const params = new URLSearchParams({
                source: dataSource,
                start: startDate.toISOString(),
                end: endDate.toISOString()
            })
            return axios.get(`/datapoint/all?${params.toString()}`)
                .then(response => {
                    const points = response.data;
                    this.dataPoints.push(...points)
                    this.dataSources.push(dataSource)
                    return points;
                });
        },
        listen(dataSources, handler) {
            const eventSource = datapointService.listen(dataSources);
            eventSource.addEventListener('message', ev => {
                const dataPoint = JSON.parse(ev.data);
                this.dataPoints.push(dataPoint);
                handler(dataPoint);
            });
        },
        clear() {
            this.dataPoints = [];
        }
    }
})
