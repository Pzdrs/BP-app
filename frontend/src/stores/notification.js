import {defineStore} from 'pinia'
import axios from "@/axios";

export const useNotificationStore = defineStore('notification', {
    state: () => ({
        notifications: [],
    }),
    getters: {
        hasNotifications() {
            return this.notifications.length > 0;
        }
    },
    actions: {
        async loadNotifications() {
            const {data} = await axios.get('/notification');
            this.notifications = data;
        },
        listenForNotifications() {
            new EventSource('/notification/live').addEventListener('message', (e) => {
                console.log(e)
            });
        },
        dismissNotification(id) {
            return axios.delete(`/notification/${id}`);
        }
    }
})
