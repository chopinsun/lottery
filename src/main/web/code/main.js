import Vue from 'vue'
import App from './App.vue'
import axios from 'axios'
import Vuetify from 'vuetify/lib'
import vuetify from './plugins/vuetify'
import map from './plugins/map'
import VCharts from 'v-charts'

Vue.config.productionTip = false
Vue.prototype.$axios = axios //全局注册，使用方法为:this.$axios
Vue.use(Vuetify)
Vue.use(VCharts)

new Vue({
  vuetify,
  map,
  render: h => h(App)
}).$mount('#app')
