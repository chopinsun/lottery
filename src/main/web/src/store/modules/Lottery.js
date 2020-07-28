import { lottery } from '../../store/types'

const state = {
  mod: '',
  type: '',
  num: '',
}

const getters = {
  [lottery.G.CONFIG]: (state) => {
    return {
      mod: state.mod,
      type: state.type,
      num: state.type,
    }
  },
}

const mutations = {
  [lottery.SET_MOD](state, mod) {
    state.mod = mod
  },
  [lottery.SET_TYPE](state, type) {
    state.type = type
  },
  [lottery.SET_NUM](state, num) {
    state.num = num
  },
  [lottery.SET_CONFIG](state, config) {
    state.mod = config.mod
    state.type = config.type
    state.num = config.num
  },
}

const actions = {
  [lottery.REFRESH]() {},
}

export default {
  state,
  getters,
  mutations,
}
