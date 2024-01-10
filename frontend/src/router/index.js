import {createRouter, createWebHistory} from 'vue-router'
import {useUserStore} from "@/stores/user";

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
            path: '/',
            component: () => import('../views/LoggedInView.vue'),
            meta: {
                signedInOnly: true
            },
            children: [
                {
                    path: '',
                    name: 'Dashboard',
                    meta: {
                        nav: true,
                        icon: 'fas fa-table-columns'
                    },
                    component: () => import('../views/MapView.vue')
                },
                {
                    path: '/settings',
                    name: 'Settings',
                    meta: {
                        nav: true,
                        icon: 'fas fa-cog'
                    },
                    component: () => import('../views/ApplicationSettingsView.vue')
                },
                {
                    path: '/users',
                    name: 'Users',
                    meta: {
                        nav: true,
                        icon: 'fas fa-user'
                    },
                    component: () => import('../views/UserManagementView.vue')
                },
            ]
        }
    ]
});

router.beforeEach(async (to, from, next) => {
    const userStore = useUserStore();

    await userStore.ensureAuthentication();

    // Set page title
    document.title = `${to.name || 'Welcome'} | ES-GPS`;

    if (userStore.isAuthenticated) {
        if (to.meta && to.meta.anonymousOnly) {
            next(from);
            return;
        }
    } else {
        if (to.meta && to.meta.signedInOnly) {
            next('/login');
            return;
        }
    }

    next();
});

export default router
