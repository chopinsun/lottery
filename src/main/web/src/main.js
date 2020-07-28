import Vue from 'vue'
import router from '@router'
import Vuex from 'vuex'
import store from '@store'

import App from '@components/app/App.vue'
import http from '@lib/http'
import vuetify from '@lib/vuetify'
import map from '@plugins/map'
import VCharts from 'v-charts'

import '@plugins/charts-theme/lottery'
import '@/utils/alert'

Vue.use(Vuex)

Vue.config.productionTip = false
Vue.prototype.ajax = http.ajax
Vue.prototype.get = http.get
Vue.prototype.post = http.post
Vue.prototype.$http = http
Vue.use(VCharts)

new Vue({
  vuetify,
  map,
  store,
  router,
  render: (h) => h(App),
}).$mount('#app')
