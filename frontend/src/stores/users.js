import {defineStore} from 'pinia'
import axios from "@/axios";

export const useUsersStore = defineStore('users', {
    state: () => ({
        users: []
    }),
    getters: {
        getUser: (state) => (id) => {
            return state.users.find(user => user.id === id);
        }
    },
    actions: {
        async loadUsers(currentUserId) {
            if (this.users.length > 0) return;
            const {data} = await axios.get('/user/all');
            this.users = data;
            this.users.sort((a, b) => {
                if (a.id === currentUserId) return -1;
                if (b.id === currentUserId) return 1;
                return 0;
            });
        },
        createUser(data) {
            return axios.post('/user/register', data)
                .then(res => {
                    this.users.push(res.data);
                });
        },
        updateUser(id, data) {
            return axios.patch(`/user/${id}`, data)
                .then(res => {
                    const index = this.users.findIndex(user => user.id === id);
                    this.users.splice(index, 1, res.data);
                });
        },
        deleteUser(id) {
            return axios.delete(`/user/${id}`)
                .then(_ => {
                    this.users = this.users.filter(user => user.id !== id);
                });
        }
    }
})
