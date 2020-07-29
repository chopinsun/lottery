import { nav } from '../../store/types'

const state = {
  currentPage: '',
  showTop: true,
  showBot: true,
}

const getters = {
  [nav.G.CURRENT_PAGE]: (state) => state.currentPage,
  [nav.G.SHOWTOPNAV]: (state) => state.showTop,
  [nav.G.SHOWBOTNAV]: (state) => state.showBot,
}

const mutations = {
  [nav.CHOOSE_PAGE](state, page) {
    state.currentPage = page
  },
  [nav.HIDE_TOP](state) {
    state.showTop = false
  },
  [nav.SHOW_TOP](state) {
    state.showTop = true
  },
  [nav.HIDE_BOT](state) {
    state.showBot = false
  },
  [nav.SHOW_BOT](state) {
    state.showBot = true
  },
}

export default {
  state,
  getters,
  mutations,
}
