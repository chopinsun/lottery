<template>
  <div class="account">
    <v-card outlined rounded="false" height="100%">
      <v-btn icon dark x-large @click="goBack()">
        <v-icon>mdi-chevron-left</v-icon>
      </v-btn>
      <v-card-title>
        <div style="height:120px;">
          <v-avatar height="120" width="120">
            <v-img src="../asset/head.jpg" alt="head" />
          </v-avatar>
        </div>
        <div class="welcome">Welcome</div>
      </v-card-title>
      <v-card-text>
        <v-text-field
          placeholder="用户名"
          prepend-inner-icon="mdi-account-outline"
          solo
          rounded
          v-model="username"
        ></v-text-field>
        <v-text-field
          placeholder="密码"
          prepend-inner-icon="mdi-lock-outline"
          :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
          :type="show1 ? 'text' : 'password'"
          @click:append="show1 = !show1"
          v-on:keydown.enter="login()"
          solo
          rounded
          v-model="password"
        ></v-text-field>
        <v-row style="margin:0">
          <v-col cols="4" style="text-align:left;padding:0;">
            <a href="#" style="color:#eee;text-decoration:none;" @click="goRegister">立即注册</a>
          </v-col>
          <v-col cols="8" style="text-align:right;padding:0;">
            <a href="#" style="color:#eee;text-decoration:none;" @click="forgetPwd">忘记密码？</a>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-btn block color="blue-grey darken-4" dark rounded height="50" @click="login()">登录</v-btn>
      </v-card-actions>
      <v-list style="background: transparent;">
        <v-list-item>
          <v-list-item-title style="text-align:center;font-size:14px;color:#eee;">第三方登录</v-list-item-title>
        </v-list-item>
        <v-list-item>
          <v-list-item-group style="margin:0 auto;">
            <a href="#" style="padding:0 5px;">
              <img
                src="../asset/weixin.png"
                height="40"
                width="40"
                style="background-color:#fff;border-radius:40px;"
              />
            </a>
          </v-list-item-group>
        </v-list-item>
      </v-list>
    </v-card>
  </div>
</template>

<script>
import { mapState, mapMutations, mapGetters } from 'vuex'
import { nav, account } from '@store/types'
import service from '@service/AccountService'
export default {
  data() {
    return {
      username: '',
      password: '',
      show1: false,
    }
  },
  mounted() {
    this.hiddenTopNav()
    this.hiddenBotNav()
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.account,
    }),
  },
  methods: {
    async login() {
      const result = await service.login({
        username: this.username,
        password: this.$md5(this.password),
      })
      this.saveUserInfo(result)
      this.$router.push('/home')
    },
    async logout() {
      const result = await service.logout({
        username: this.username,
        password: this.password,
      })
    },
    goRegister() {
      this.$alert.error('暂未开放')
    },
    forgetPwd() {
      this.$alert.info('暂未开放')
    },
    goWechart() {
      this.$alert.info('暂未开放')
    },
    goBack() {
      if (window.history.length) {
        this.$router.go(-1)
      } else {
        this.$router.push('/main/home')
      }
    },
    ...mapMutations({
      hiddenTopNav: nav.HIDE_TOP,
      hiddenBotNav: nav.HIDE_BOT,
      saveUserInfo: account.SAVE_USERINFO,
    }),
  },
}
</script>


<style scoped>
.account {
  background-image: url(../asset/login-bg.jpeg);
  background-size: cover;
  background-position: center;
  height: 100%;
  position: relative;
  z-index: 1;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
}
.account::before {
  content: '';
  display: block;
  position: absolute;
  z-index: -1;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background: #005bea;
  background: -webkit-linear-gradient(bottom, #885bea, #00c6fb);
  background: -o-linear-gradient(bottom, #885bea, #00c6fb);
  background: -moz-linear-gradient(bottom, #885bea, #00c6fb);
  background: linear-gradient(bottom, #885bea, #00c6fb);
  opacity: 0.9;
}
.v-card {
  background: transparent !important;
  border: none !important;
  min-height: 660px;
}
.v-card__title {
  height: 200px;
  display: block;
  text-align: center;
  font-size: 28px;
}
.welcome {
  font-family: Montserrat-ExtraBold;
  font-size: 24px;
  color: #fff;
  line-height: 1.2;
  text-align: center;
  width: 100%;
  display: block;
  padding: 0 10px;
}
</style>