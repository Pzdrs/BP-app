import {defineStore} from 'pinia'
import axios from "@/axios";

export const useUserStore = defineStore('user', {
    state: () => ({
        details: null
    }),
    getters: {
        isAuthenticated() {
            return this.details !== null;
        },
        getCreatedAt() {
            return Date.parse(this.details.created)
        },
        getUpdatedAt() {
            return Date.parse(this.details.updated)
        },
        hasAnyRole: (state) => (roles) => {
            if (state.details === null) return false;
            return roles.some(role => role === state.details.role);
        },
        hasViewPermission: (state) => (route) => {
            if (state.details === null) return false;
            return !(route.meta.rolesAny && !route.meta.rolesAny.includes(state.details.role));
        }
    },
    actions: {
        async ensureAuthentication() {
            if (this.details === null) {
                try {
                    const response = await axios.get('/user/me');
                    this.details = response.data;
                } catch (e) {
                }
            }
        },
        signIn(data) {
            return axios.post('/auth/login', data)
                .then(response => {
                    this.details = response.data;
                })
                .catch(error => {
                    throw error;
                });
        },
        signOut() {
            return axios.post('/auth/logout')
                .then(() => {
                    this.details = null;
                })
                .catch(error => {
                    throw error;
                });
        }
    }
})
