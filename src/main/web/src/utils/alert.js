import store from '@store'

const info = (msg) => {
  store.dispatch('info', msg)
}
const success = (msg) => {
  store.dispatch('success', msg)
}
const warn = (msg) => {
  store.dispatch('warn', msg)
}
const error = (msg) => {
  store.dispatch('error', msg)
}

export default {
  info,
  success,
  warn,
  error,
}
