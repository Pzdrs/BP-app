import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'Home',
            component: () => import('../views/MapView.vue')
        },
        {
            path: '/settings',
            name: 'Settings',
            component: () => import('../views/ApplicationSettingsView.vue')
        }
    ]
})

export default router
