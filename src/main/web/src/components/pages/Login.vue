<template>
  <div class="account" style="margin: -100px -10px 0px -10px;height:600px;">
    <v-card outlined rounded="false" height="600">
      <v-card-title>登录</v-card-title>
      <v-card-subtitle>username</v-card-subtitle>
      <div style="text-align:center;">
        <v-avatar color="gray" size="80">
          <v-icon size="80">mdi-account-question</v-icon>
        </v-avatar>
      </div>

      <v-card-text>
        <v-text-field label="用户名" outlined prepend-inner-icon="mdi-account"></v-text-field>
        <v-text-field label="密码" outlined prepend-inner-icon="mdi-lock-question"></v-text-field>
        <v-row>
          <v-col cols="8">
            <a href="#">忘记密码？</a>
          </v-col>
          <v-col cols="4">
            <a href="#">
              <img src="../../asset/weixin.png" height="30" width="30" />
            </a>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-btn block color="success" dark>登录</v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script>
import { mapState, mapMutations, mapGetters } from 'vuex'
import service from '@service/AccountService'
export default {
  data() {
    return {
      username: '',
      password: '',
    }
  },
  mounted() {
    //TODO 去掉nav
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.account,
    }),
  },
  methods: {
    async login() {
      const result = await service.login({ username, password })
    },
    async logout() {
      const result = await service.logout({ username, password })
    },
  },
}
</script>