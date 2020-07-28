<template>
  <div class="alter">
    <v-alert
      text
      dense
      color="cyan"
      border="left"
      elevation="2"
      colored-border
      :type="alertinfo.level"
    >{{alertinfo.message}}</v-alert>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { alert } from '~store/types'
export default {
  data() {
    return {}
  },
  watch: {
    alertinfo() {
      setTimeout(() => {
        this.$store.comit('')
      }, 3000)
    },
  },
  computed: {
    ...mapState({
      alertinfo: (state) => state.alert,
    }),
  },
  methods: {},
}
</script>