import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/login',
            name: 'Login',
            component: () => import('../views/LoginView.vue'),
            meta: {
                anonymousOnly: true
            }
        },
        {
            path: '',
            component: () => import('../views/LoggedInView.vue'),
            meta: {
                signedInOnly: true
            },
            children: [
                {
                    path: '/',
                    name: 'Dashboard',
                    meta: {
                        nav: true,
                        icon: 'fa-table-columns'
                    },
                    component: () => import('../views/MapView.vue')
                },
                {
                    path: '/settings',
                    name: 'Settings',
                    meta: {
                        nav: true,
                        icon: 'fa-cog'
                    },
                    component: () => import('../views/ApplicationSettingsView.vue')
                },
            ]
        },

    ]
});

router.beforeEach((to, from, next) => {
    // Set page title
    document.title = `${to.name || 'Welcome'} | ES-GPS`;

    // Anonymous only pages
    if (signedIn() && to.meta && to.meta.anonymousOnly) next('');

    // Signed in only pages
    if (!signedIn() && to.meta && to.meta.signedInOnly) next('/login');

    next();
});

function signedIn() {
    return true;
}

export default router
