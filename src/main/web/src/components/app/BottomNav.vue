<template>
  <div class="bottom-nav">
    <v-bottom-navigation v-model="bottomNav" shift change="changePage" fixed>
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
    bottomNav() {
      this.setCurrentPage(currentTabComponent)
      this.$router.push(currentTabComponent)
    },
  },
  computed: {
    currentTabComponent() {
      if ('home' === this.bottomNav) {
        return 'Home'
      } else if ('history' === this.bottomNav) {
        return 'History'
      } else if ('nearby' === this.bottomNav) {
        return 'Map'
      } else if ('analysis' === this.bottomNav) {
        return 'Analysis'
      } else if ('account' === this.bottomNav) {
        return 'Account'
      } else {
        console.log('404')
        return 'Home'
      }
    },
  },
  methods: {
    ...mapMutations({
      setCurrentPage: nav.CHOOSE_PAGE,
    }),
  },
}
</script>
    
    