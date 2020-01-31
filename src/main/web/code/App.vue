<template>
  <v-app>
    <component v-bind:is="currentTabComponent"></component>
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
    <v-btn value="account">
      <span>Account</span>
      <v-icon>mdi-history</v-icon>
    </v-btn>
    </v-bottom-navigation>
  </v-app>
</template>

<script>
import HomePage from './components/HomePage.vue'
import History from './components/History.vue'
import Map from './components/Map.vue'
import Analysis from './components/Analysis.vue'
import Account from './components/Account.vue'

export default {
  name: 'app',
  components: {
    HomePage,
    History,
    Map,
    Analysis,
    Account,
  },
  data(){
    return{
       bottomNav: 'home',
    }
  },
  computed:{
    currentTabComponent(){
      if('home' === this.bottomNav){
        return 'HomePage'
      }else if('history' === this.bottomNav){
        return 'History'
      }else if('nearby' === this.bottomNav){
        return 'Map'
      }else if('analysis' === this.bottomNav){
        return 'Analysis'
      }else if('account' === this.bottomNav){
        return 'Account'
      }
      return 'HomePage'
    }
  },
  methods:{
   
  }
}
</script>
