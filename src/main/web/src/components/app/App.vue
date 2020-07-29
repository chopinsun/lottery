<template>
  <v-app>
    <app-nav></app-nav>
    <!-- <v-sheet
      id="scrolling-techniques"
      class="overflow-y-auto"
      :style="{height:screenHeight+'px','margin-top':'-24px'}"
    >-->
    <v-container id="scrolling-techniques" style="height:100%;padding:0;">
      <keep-alive>
        <router-view></router-view>
      </keep-alive>
    </v-container>
    <!-- </v-sheet> -->
    <bottom-nav></bottom-nav>
  </v-app>
</template>

<script>
import AppNav from '../app/AppNav'
import BottomNav from '../app/BottomNav'

export default {
  name: 'app',
  components: {
    AppNav,
    BottomNav,
  },
  data() {
    return {
      screenHeight: document.documentElement.clientHeight,
    }
  },
  mounted() {},
  computed: {
    resizeHeight() {
      return {
        height: window.screen.height,
      }
    },
  },
  methods: {},
}
</script>
<style lang="css">
.v-item-group.v-bottom-navigation .v-btn {
  min-width: 60px !important;
  padding: 8px;
}

.Chart {
  background: #212733;
  border-radius: 15px;
  box-shadow: 0px 2px 15px rgba(25, 25, 25, 0.27);
  margin: 25px 0;
}

.Chart h2 {
  margin-top: 0;
  padding: 15px 0;
  color: rgba(255, 0, 0, 0.5);
  border-bottom: 1px solid #323d54;
}
</style>
