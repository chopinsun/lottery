<template>
  <div class="bottom-nav" v-show="nav.showBot">
    <v-bottom-navigation v-model="bottomNav" shift v-on:change="changePage" fixed>
      <v-btn value="home">
        <span>Home</span>
        <v-icon>mdi-home</v-icon>
      </v-btn>
      <v-btn value="history">
        <span>History</span>
        <v-icon>mdi-database-search</v-icon>
      </v-btn>
      <v-btn value="nearby">
        <span>Nearby</span>
        <v-icon>mdi-map-marker</v-icon>
      </v-btn>
      <v-btn value="analysis">
        <span>Analysis</span>
        <v-icon>mdi-chart-line</v-icon>
      </v-btn>
    </v-bottom-navigation>
  </div>
</template>

<script>
import { mapState, mapMutations, mapGetters } from 'vuex'
import { nav } from '@store/types'
export default {
  data() {
    return {
      bottomNav: 'home',
    }
  },
  watch: {
    $route(to, from) {
      this.bottomNav = this.pathToName(to)
      console.log(to, this.bottomNav)
      // this.setCurrentPage(this.bottomNav)
    },
  },
  computed: {
    ...mapState({
      nav: (state) => state.nav,
    }),
  },
  methods: {
    ...mapMutations({
      setCurrentPage: nav.CHOOSE_PAGE,
    }),
    changePage(page) {
      console.log(page, this.nameToPath(page))
      this.setCurrentPage(page)
      if (this.nameToPath(page) !== this.$router.path) {
        this.$router.push(this.nameToPath(page))
      }
    },
    nameToPath(name) {
      if ('home' === name) {
        return '/main/home'
      } else if ('history' === name) {
        return '/main/history'
      } else if ('nearby' === name) {
        return '/main/map'
      } else if ('analysis' === name) {
        return '/main/analysis'
      } else {
        console.log('4041')
        return '/main/home'
      }
    },
    pathToName(path) {
      if ('/main/home' === path) {
        return 'home'
      } else if ('/main/history' === path) {
        return 'history'
      } else if ('/main/nearby' === path) {
        return 'map'
      } else if ('/main/analysis' === path) {
        return 'analysis'
      } else {
        console.log('4042')
        return 'home'
      }
    },
  },
}
</script>
    
    