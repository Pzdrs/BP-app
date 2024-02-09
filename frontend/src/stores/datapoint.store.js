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
        fetchDataPoints(dataSource) {
            return axios.get(`/datapoint/all?source=${dataSource}`).then(response => {
                this.dataPoints.push(...response.data)
                this.dataSources.push(dataSource)
            })
        },
        clear(dataSourcesToKeep) {
            this.dataPoints = this.dataPoints.filter(point => {
                return dataSourcesToKeep.includes(point.source)
            })
        }
    }
})
