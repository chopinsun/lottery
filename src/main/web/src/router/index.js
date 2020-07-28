import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  mode: 'hash',
  routes: [
    {
      path: '/',
      redirect: '/home',
    },
    {
      path: '/home',
      component: () => import('@components/pages/Home'),
    },
    {
      path: '/map',
      component: () => import('@components/pages/Map'),
    },
    {
      path: '/history',
      component: () => import('@components/pages/History'),
    },
    {
      path: '/login',
      component: () => import('@components/pages/Login'),
    },
    {
      path: '/account',
      component: () => import('@components/pages/Account'),
    },
    {
      path: '/analysis',
      component: () => import('@components/pages/Analysis'),
    },
  ],
})
