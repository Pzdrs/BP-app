import {defineStore} from 'pinia'
import datasourceService from "@/services/datasource.service";
import axios from "@/axios";

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
            return axios.get(`/datapoint/all?${params.toString()}`).then(response => {
                this.dataPoints.push(...response.data)
                this.dataSources.push(dataSource)
            })
        },
        clear() {
            this.dataPoints = [];
        }
    }
})
