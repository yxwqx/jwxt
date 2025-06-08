import { createRouter, createWebHistory } from 'vue-router'
import AdminAuth from '@/views/AdminAuth.vue'
import Dashboard from '@/views/Dashboard.vue'

const routes = [
    { path: '/', redirect: '/login' },
    { path: '/login', component: AdminAuth },
    {
        path: '/dashboard/:page?',
        component: Dashboard
    }
]

export default createRouter({
    history: createWebHistory(),
    routes
})
