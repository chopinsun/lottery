import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  mode: 'hash',
  routes: [
    {
      path: '/',
      redirect: '/main/home',
    },
    {
      path: '/main',
      component: () => import('@views/Main'),
      children: [
        {
          path: 'home',
          component: () => import('@views/Home'),
        },
        {
          path: 'map',
          component: () => import('@views/Map'),
        },
        {
          path: 'history',
          component: () => import('@views/History'),
        },

        {
          path: 'analysis',
          component: () => import('@views/Analysis'),
        },
      ],
    },
    {
      path: '/login',
      component: () => import('@views/Login'),
    },
    {
      path: '/account',
      component: () => import('@views/Account'),
    },
  ],
})
