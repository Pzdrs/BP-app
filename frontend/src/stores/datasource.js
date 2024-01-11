import {defineStore} from 'pinia'
import axios from "@/axios";

export const useDataSourceStore = defineStore('data_source', {
    state: () => ({
        dataSources: []
    }),
    getters: {
        getAdoptedDataSources: (state) => {
            return state.dataSources.filter(dataSource => dataSource.adopted);
        },
        getAdoptionAwaitingDataSources: (state) => {
            return state.dataSources.filter(dataSource => !dataSource.adopted);
        }
    },
    actions: {
        async loadDataSources() {
            if (this.dataSources.length > 0) return;
            const {data} = await axios.get('/datasource/all');
            this.dataSources = data;
        },
        updateDataSource(id, data) {
            return axios.patch(`/datasource/${id}`, data)
                .then(res => {
                    this.dataSources = this.dataSources.filter(dataSource => dataSource.id !== id);
                    this.dataSources.push(res.data);
                });
        },
        deleteDataSource(id) {
            return axios.delete(`/datasource/${id}`)
                .then(_ => {
                    this.dataSources = this.dataSources.filter(dataSource => dataSource.id !== id);
                });
        },
        adoptDataSource(id, data) {
            return axios.post(`/datasource/${id}/adopt`, data)
                .then(res => {
                    this.dataSources = this.dataSources.filter(dataSource => dataSource.id !== id);
                    this.dataSources.push(res.data);
                });
        }
    }
})
