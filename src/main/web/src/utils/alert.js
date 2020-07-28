export default {
  install(Vue) {
    Vue.prototype.$alert = {
      info(msg) {
        this.$store.dispatch('info', msg)
      },
      success(msg) {
        this.$store.dispatch('success', msg)
      },
      warn(msg) {
        this.$store.dispatch('warn', msg)
      },
      error(msg) {
        this.$store.dispatch('error', msg)
      },
    }
  },
}
