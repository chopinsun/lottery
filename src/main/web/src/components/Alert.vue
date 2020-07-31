<template>
  <div class="alert">
    <v-alert
      border="left"
      dark
      v-model="alertinfo.display"
      :type="alertinfo.level"
    >{{alertinfo.message}}</v-alert>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { alert } from '@store/types'
export default {
  data() {
    return {}
  },
  computed: {
    ...mapState({
      alertinfo: (state) => state.alert,
    }),
  },
  methods: {},
}
</script>

<style>
.alert .v-alert {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 999;
  background-color: #fff;
  width: 100%;
}
</style>