import {defineStore} from 'pinia'
import notificationService from "@/services/notification.service";

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
            this.notifications = await notificationService.getAll();
        },
        listenForNotifications() {
            const eventSource = notificationService.listen();
        },
        dismissNotification(id) {
            return notificationService.dismiss(id);
        }
    }
})
