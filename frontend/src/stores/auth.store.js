import {defineStore} from 'pinia'
import authService from "@/services/auth.service";
import {getFullName} from "@/utils/user";

export const useAuthStore = defineStore('auth', {
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
        },
        getFullName: (state) => {
            return getFullName(state.details);
        }
    },
    actions: {
        /**
         * Fetches the user profile. If this request fails, the user is considered unauthenticated.
         * @returns {Promise<void>}
         */
        async fetchProfile() {
            if (this.details === null) {
                this.details = await authService.getProfile();
            }
        },
        /**
         * Attempts to sign in the user.
         * @param data
         * @returns {Promise<axios.AxiosResponse<any>>}
         */
        signIn(data) {
            return authService.login(data.username, data.password)
                .then(response => this.details = response.data);
        },
        /**
         * Attempts to sign out the user.
         * @returns {Promise<axios.AxiosResponse<any>>}
         */
        signOut() {
            return authService.logout()
                .then(() => this.details = null);
        }
    }
})
