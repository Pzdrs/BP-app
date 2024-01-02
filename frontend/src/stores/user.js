import {defineStore} from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        firstName: 'John',
        lastName: 'Doe',
        email: 'petr.bohac@remeslovkostce.cz',
    }),
    actions: {
        signIn() {

        }
    }
})
