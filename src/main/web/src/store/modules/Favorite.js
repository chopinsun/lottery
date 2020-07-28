import { favorite } from '../../store/types'

const state = {
  list: [],
}

const getters = {
  [favorite.G.LIST]: (state) => state.list,
}

const mutations = {
  [favorite.PUSH](state, item) {
    state.list.push(item)
  },
  [favorite.SET](state, items) {
    state.list = items
  },
  [favorite.REMOVE](state, item) {
    const index = state.list.indexOf(item)
    if (index != -1) {
      state.list.splice(index, 1)
    }
  },
  [favorite.CLEAR](state) {
    state.list = []
  },
}

export default {
  state,
  getters,
  mutations,
}
