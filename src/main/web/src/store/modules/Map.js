import { map } from '../../store/types'

const state = {
  range: '',
}

const getters = {
  [map.G.RANGE]: (state) => state.range,
}

const mutations = {
  [map.SET_RANGE](state, range) {
    state.range = range
  },
}

export default {
  state,
  getters,
  mutations,
}
