import {defineStore} from 'pinia'
import datasourceService from "@/services/datasource.service";
import axios from "@/axios";
import notificationService from "@/services/notification.service";
import datapointService from "@/services/datapoint.service";

export const useDataPointStore = defineStore('data_point', {
    state: () => ({
        dataPoints: [],
        dataSources: [],
        monthlyCounts: []
    }),
    getters: {
        getLatLngs: (state) => {
            return state.dataPoints.map(point => {
                return [point.lat, point.lng]
            })
        },
        getDistinctBreakdownYears: (state) => {
            return state.monthlyCounts.map(breakdown => breakdown.year)
                .filter((value, index, self) => self.indexOf(value) === index);
        },
        getBreakdown: (state) => {
            return year => {
                const currentYearCounts = state.monthlyCounts.filter(count => count.year === year);
                let newCounts = Array(12).fill(0);

                for (const count of currentYearCounts) {
                    newCounts[count.month - 1] = count.count;
                }

                return newCounts;
            }
        },
        getInitialBreakdownYear: (state) => {
            const currentYear = new Date().getFullYear();
            const years = state.monthlyCounts.map(count => count.year);
            return years.reduce((prev, curr) => Math.abs(curr - currentYear) < Math.abs(prev - currentYear) ? curr : prev);
        }
    },
    actions: {
        fetchDataPoints(dataSource, startDate, endDate) {
            const params = new URLSearchParams({
                source: dataSource,
                start: startDate.toISOString(),
                end: endDate.toISOString()
            });
            return axios.get(`/datapoint/all?${params.toString()}`)
                .then(response => {
                    const points = response.data;
                    this.dataPoints.push(...points)
                    this.dataSources.push(dataSource)
                    return points;
                });
        },
        fetchMonthlyBreakdown() {
            return datapointService.getMonthlyBreakdown().then(response => {
                this.monthlyCounts = response.data;
                return response.data;
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
