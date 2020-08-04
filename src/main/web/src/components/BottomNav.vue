<template>
  <div class="bottom-nav">
    <v-bottom-navigation
      v-model="bottomNav"
      shift
      v-on:change="changePage"
      fixed
      :input-value="nav.showBot"
    >
      <v-btn value="home" :class="navClass.home">
        <span>Home</span>
        <v-icon>mdi-home</v-icon>
      </v-btn>
      <v-btn value="history" :class="navClass.history">
        <span>History</span>
        <v-icon>mdi-database-search</v-icon>
      </v-btn>
      <v-btn value="nearby" :class="navClass.nearby">
        <span>Nearby</span>
        <v-icon>mdi-map-marker</v-icon>
      </v-btn>
      <v-btn value="analysis" :class="navClass.analysis">
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
      navClass: {
        home: '',
        history: '',
        nearby: '',
        analysis: '',
      },
    }
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
      console.log(path, path == '/main/history')
      if ('/main/home' == path) {
        return 'home'
      } else if ('/main/history' == path) {
        console.log(1111)
        return 'history'
      } else if ('/main/map' == path) {
        return 'nearby'
      } else if ('/main/analysis' == path) {
        return 'analysis'
      } else {
        return 'home'
      }
    },
  },
}
</script>
    
    