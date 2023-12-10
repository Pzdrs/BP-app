import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/login',
            name: 'Login',
            component: () => import('../views/LoginView.vue')
        },
        {
            path: '',
            component: () => import('../views/LoggedInView.vue'),
            children: [
                {
                    path: '',
                    name: 'Home',
                    component: () => import('../views/MapView.vue')
                },
                {
                    path: '/settings',
                    name: 'Settings',
                    component: () => import('../views/ApplicationSettingsView.vue')
                },
            ]
        },

    ]
})

export default router
