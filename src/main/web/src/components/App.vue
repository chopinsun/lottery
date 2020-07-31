<template>
  <v-app>
    <alert></alert>
    <router-view></router-view>
  </v-app>
</template>

<script>
import Alert from '@components/Alert'
export default {
  name: 'app',
  components: {
    Alert,
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
