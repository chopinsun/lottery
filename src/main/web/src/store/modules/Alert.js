import { alert } from '@store/types'

const TIMEOUT = 3000
const state = {
  level: 'info',
  message: '',
  display: false,
}

const getters = {
  [alert.G.LEVEL]: (state) => state.level,
  [alert.G.MESSAGE]: (state) => state.message,
  [alert.G.DISPLAY]: (state) => state.display,
}

const mutations = {
  [alert.PUSH](state, config) {
    if (config.message) {
      state.message = config.message
      state.level = config.level
      state.display = true
    }
  },
  [alert.REMOVE](state) {
    state.level = 'info'
    state.message = ''
    state.display = false
  },
}

const actions = {
  info({ commit }, message) {
    commit(alert.PUSH, { level: 'info', message: message })
    setTimeout(() => {
      commit(alert.REMOVE)
    }, TIMEOUT)
  },
  error({ commit }, message) {
    commit(alert.PUSH, { level: 'error', message: message })
    setTimeout(() => {
      commit(alert.REMOVE)
    }, TIMEOUT)
  },
  success({ commit }, message) {
    commit(alert.PUSH, { level: 'success', message: message })
    setTimeout(() => {
      commit(alert.REMOVE)
    }, TIMEOUT)
  },
  warn({ commit }, message) {
    commit(alert.PUSH, { level: 'warn', message: message })
    setTimeout(() => {
      commit(alert.REMOVE)
    }, TIMEOUT)
  },
}

export default {
  state,
  getters,
  mutations,
  actions,
}
