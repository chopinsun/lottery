<template>
  <div class="main">
    <app-nav></app-nav>
    <v-sheet id="scrolling-techniques" class="overflow-y-auto">
      <v-container
        :style="{height: resizedHeight+'px','margin-top':'96px','margin-bottom':'60px','padding':0}"
      >
        <transition>
          <keep-alive>
            <router-view></router-view>
          </keep-alive>
        </transition>
      </v-container>
    </v-sheet>

    <bottom-nav></bottom-nav>
  </div>
</template>

<script>
import AppNav from '@components/AppNav'
import BottomNav from '@components/BottomNav'

export default {
  components: {
    AppNav,
    BottomNav,
  },
  data() {
    return {
      screenHeight: document.documentElement.clientHeight,
    }
  },
  computed: {
    resizedHeight() {
      return screen.height - 152
    },
  },
}
</script>

<style scoped>
#scrolling-techniques {
  background: #fff;
}
</style>