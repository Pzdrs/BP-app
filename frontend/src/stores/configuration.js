import {defineStore} from 'pinia'
import configurationService from "@/services/configuration.service";

export const useConfigurationStore = defineStore('configuration', {
    state: () => ({
        mqtt: {}
    }),
    actions: {
        async loadMqttConfig() {
            this.mqtt = await configurationService.getMqtt();
        },
        async updateMqttConfig(data) {
            return await configurationService.updateMqtt(data)
                .then(res => {
                    this.mqtt = res.data;
                });
        }
    }
})
