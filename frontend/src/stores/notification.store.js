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
            eventSource.addEventListener('message', (e) => {
                this.notifications.push(JSON.parse(e.data));
            });
        },
        dismissNotification(id) {
            return notificationService.dismiss(id)
                .then(_ => {
                    this.notifications = this.notifications.filter(notification => notification.id !== id);
                });
        }
    }
})
