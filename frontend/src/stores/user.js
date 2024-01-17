import {defineStore} from 'pinia'
import userService from "@/services/user.service";
import {useAuthStore} from "@/stores/auth";


export const useUserStore = defineStore('user', {
    state: () => ({
        users: [],
        roles: []
    }),
    getters: {
        getUser: (state) => (id) => {
            return state.users.find(user => user.id === id);
        }
    },
    actions: {
        async loadUsers(currentUserId) {
            if (this.users.length > 0) return;
            this.users = await userService.getAll();
            this.users.sort((a, b) => {
                if (a.id === currentUserId) return -1;
                if (b.id === currentUserId) return 1;
                return 0;
            });
            await this.loadRoles();
        },
        async loadRoles() {
            if (this.roles.length > 0) return;
            this.roles = await userService.getRoles();
        },
        async createUser(data) {
            return await userService.create(data)
                .then(res => {
                    this.users.push(res.data);
                });
        },
        async updateUser(id, data) {
            const authStore = useAuthStore();
            return await userService.update(id, data)
                .then(res => {
                    const index = this.users.findIndex(user => user.id === id);
                    this.users.splice(index, 1, res.data);

                    // Update authStore if the current user is updated
                    if (id === authStore.details.id) authStore.details = res.data;
                });
        },
        async deleteUser(id) {
            return await userService.delete(id)
                .then(_ => {
                    this.users = this.users.filter(user => user.id !== id);
                });
        }
    }
})
