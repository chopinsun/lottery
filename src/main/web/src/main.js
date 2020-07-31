import Vue from 'vue'
import router from '@router'
import Vuex from 'vuex'
import store from '@store'

import App from '@components/App.vue'
import http from '@lib/http'
import vuetify from '@lib/vuetify'
import map from '@plugins/map'
import VCharts from 'v-charts'

import '@plugins/charts-theme/lottery'
import alert from '@/utils/alert'

import md5 from 'js-md5'

Vue.use(Vuex)

Vue.config.productionTip = false
Vue.prototype.ajax = http.ajax
Vue.prototype.get = http.get
Vue.prototype.post = http.post
Vue.prototype.$http = http
Vue.prototype.$md5 = md5
Vue.prototype.$alert = alert
Vue.use(VCharts)
window.alert = alert
window.$alert = alert

new Vue({
  vuetify,
  map,
  store,
  router,
  render: (h) => h(App),
}).$mount('#app')
