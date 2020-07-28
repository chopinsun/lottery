import { history } from '../../store/types'

const state = {
  from: '',
  to: '',
  limit: '10',
}

const getters = {
  [history.G.FROM]: (state) => state.from,
  [history.G.TO]: (state) => state.to,
  [history.G.LIMIT]: (state) => state.limit,
}

const mutations = {
  [history.SET_LIMT](state, limit) {
    state.limit = limit
  },
  [history.SET_FROM](state, from) {
    state.limit = limit
  },
  [history.SET_TO](state, to) {
    state.limit = limit
  },
}

export default {
  state,
  getters,
  mutations,
}
