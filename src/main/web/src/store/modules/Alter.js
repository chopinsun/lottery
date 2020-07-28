import { alter } from '../../store/types'

const TIMEOUT = 3000
const state = {
  level: '',
  message: '',
}

const getters = {
  [alter.G.LEVEL]: (state) => state.level,
  [alter.G.MESSAGE]: (state) => state.message,
}

const mutations = {
  [alert.PUSH](state, level, message) {
    state.level = level
    state.message = message
  },
  [alert.REMOVE](state) {
    ;(state.level = ''), (state.message = '')
  },
}

const actions = {
  info({ commit }, message) {
    commit(alert.PUSH, 'info', message)
    setTimeout(() => {
      commit(alert.REMOVE)
    }, TIMEOUT)
  },
  error() {
    commit(alert.PUSH, 'error', message)
    setTimeout(() => {
      commit(alert.REMOVE)
    }, TIMEOUT)
  },
  success() {
    commit(alert.PUSH, 'success', message)
    setTimeout(() => {
      commit(alert.REMOVE)
    }, TIMEOUT)
  },
  warn() {
    commit(alert.PUSH, 'warn', message)
    setTimeout(() => {
      commit(alert.REMOVE)
    }, TIMEOUT)
  },
}

export default {
  state,
  getters,
  mutations,
}
