import {defineStore} from 'pinia'
import datasourceService from "@/services/datasource.service";

export const useDataSourceStore = defineStore('data_source', {
    state: () => ({
        dataSources: [],
        groups: []
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
            this.dataSources = await datasourceService.getAll();
        },
        async loadGroups() {
            if (this.groups.length > 0) return;
            this.groups = await datasourceService.getGroups();
        },
        updateDataSource(id, data) {
            return datasourceService.update(id, data)
                .then(res => {
                    const index = this.dataSources.findIndex(dataSource => dataSource.id === id);
                    this.dataSources.splice(index, 1, res.data);
                });
        },
        deleteDataSource(id) {
            return datasourceService.delete(id)
                .then(_ => {
                    this.dataSources = this.dataSources.filter(dataSource => dataSource.id !== id);
                });
        },
        adoptDataSource(id, data) {
            return datasourceService.adopt(id, data)
                .then(res => {
                    this.dataSources = this.dataSources.filter(dataSource => dataSource.id !== id);
                    this.dataSources.push(res.data);
                });
        }
    }
})
