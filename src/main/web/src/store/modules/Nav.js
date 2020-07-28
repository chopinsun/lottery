import {nav} from '../../store/types'

const state={
    currentPage: "",
}

const getters={
    [nav.G.CURRENT_PAGE]: state => state.currentPage,
}

const mutations = {
    [nav.CHOOSE_PAGE] (state,page){
        state.currentPage = page
    },
}


export default {
    state,
    getters,
    mutations
  }

