import { account } from '../../store/types'

const state = {
  userinfo: {},
  authority: {},
  config: {},
  favorite: {},
}

const getters = {}

const mutations = {
  [account.SAVE_USERINFO](state, userinfo) {
    state.userinfo = userinfo
  },
  [account.SAVE_AUTHOTITY](state, authority) {
    state.authority = authority
  },
  [account.SAVE_CONFIG](state, config) {
    state.config = config
  },
  [account.SAVE_FAVORITE](state, favorite) {
    state.favorite = favorite
  },
}

export default {
  state,
  getters,
  mutations,
}
