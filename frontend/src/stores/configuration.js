import {defineStore} from 'pinia'
import axios from "@/axios";

export const useConfigurationStore = defineStore('configuration', {
    state: () => ({
        mqtt: {
            host: null,
            port: null,
            username: null,
            password: null,
        }
    }),
    actions: {
        async loadMqttConfig() {
            const response = await axios.get('/config/mqtt');
            this.mqtt = response.data;
        },
        updateMqttConfig(data) {
            return axios.post('/config/mqtt', data);
        }
    }
})
