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
        async loadUsers() {
            const {data} = await axios.get('/user/all');
            this.users = data;
        },
        createUser(data) {
            return axios.post('/user/register', data)
                .then(res => {
                    this.users.push(res.data);
                });
        },
        updateUser(id, data) {
            return axios.patch(`/user/${id}`, data);
        },
        deleteUser(id) {
            return axios.delete(`/user/${id}`)
                .then(_ => {
                    this.users = this.users.filter(user => user.id !== id);
                });
        }
    }
})
