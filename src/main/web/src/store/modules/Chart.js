import { chart } from '../../store/types'

const state = {
  mod: '',
}

const getters = {
  [chart.G.MOD]: (state) => state.mod,
}

const mutations = {
  [chart.SET_MOD](state, mod) {
    state.mod = mod
  },
}

export default {
  state,
  getters,
  mutations,
}
