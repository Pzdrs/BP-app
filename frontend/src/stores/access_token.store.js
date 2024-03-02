import {defineStore} from 'pinia'
import tokenService from "@/services/token.service";


export const useTokenStore = defineStore('access_token', {
    state: () => ({
        tokens: []
    }),
    getters: {
        isNoTokens: state => state.tokens.length === 0
    },
    actions: {
        async loadTokens() {
            if (this.tokens.length > 0) return;
            this.tokens = await tokenService.getAll();
        },
        async issue(data) {
            return await tokenService.issue(data)
                .then(res => {
                    this.tokens.push(res.data);
                });
        },
        async updateToken(id, data) {
            return await tokenService.update(id, data)
                .then(res => {
                    this.tokens = this.tokens.map(token => token.id === id ? res.data : token);
                });
        },
        async deleteToken(id) {
            return await tokenService.delete(id)
                .then(_ => {
                    this.tokens = this.tokens.filter(token => token.id !== id);
                });
        }
    }
})
