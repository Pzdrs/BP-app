import {createRouter, createWebHistory} from 'vue-router'
import {useAuthStore} from "@/stores/auth.store";
import PageNotFoundView from "@/views/PageNotFoundView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/login',
            name: 'Login',
            component: () => import('../views/LoginView.vue'),
            meta: {
                unauthenticatedOnly: true
            }
        },
        {
            path: '/',
            component: () => import('../components/layout/AuthenticatedBaseLayout.vue'),
            meta: {
                authenticatedOnly: true
            },
            children: [
                {
                    path: '',
                    name: 'Dashboard',
                    meta: {
                        nav: true,
                        icon: 'fas fa-table-columns',
                        rolesAny: ['ADMINISTRATOR', 'USER']
                    },
                    component: () => import('../views/MapView.vue')
                },
                {
                    path: '/settings',
                    name: 'Settings',
                    meta: {
                        nav: true,
                        icon: 'fas fa-cog',
                        rolesAny: ['ADMINISTRATOR']
                    },
                    component: () => import('../views/ApplicationSettingsView.vue')
                },
                {
                    path: '/data-sources',
                    name: 'Data sources',
                    meta: {
                        nav: true,
                        icon: 'fas fa-location-dot',
                        rolesAny: ['ADMINISTRATOR']
                    },
                    component: () => import('../views/DataSourcesView.vue')
                }, {
                    path: '/users',
                    name: 'Users',
                    meta: {
                        nav: true,
                        icon: 'fas fa-users',
                        rolesAny: ['ADMINISTRATOR']
                    },
                    component: () => import('../views/UserManagementView.vue')
                }, {
                    path: '/tokens',
                    name: 'Access tokens',
                    meta: {
                        nav: true,
                        icon: 'fa-solid fa-key',
                        rolesAny: ['ADMINISTRATOR', 'USER']
                    },
                    component: () => import('../views/AccessTokenManagementView.vue')
                }, {
                    path: '/profile',
                    name: 'My profile',
                    meta: {
                        nav: true,
                        icon: 'fas fa-user',
                        rolesAny: ['USER', 'ADMINISTRATOR']
                    },
                    component: () => import('../views/ProfileView.vue')
                }
            ]
        },
        {path: '/:pathMatch(.*)*', component: PageNotFoundView},
    ]
});

router.beforeEach(async (to, from, next) => {
    const authStore = useAuthStore();

    await authStore.fetchProfile();

    if (authStore.isAuthenticated) {
        if (to.meta.unauthenticatedOnly) {
            next(from);
            return;
        }

        if (to.meta.rolesAny && !authStore.hasAnyRole(to.meta.rolesAny)) {
            next(from);
            return;
        }
    } else {
        if (to.meta.authenticatedOnly) {
            next('/login');
            return;
        }
    }

    next();
    document.title = `ES GPS | ${to.name || 'Welcome'}`;
});

export default router
