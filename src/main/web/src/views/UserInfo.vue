<template>
  <div class="account">
    <v-card outlined rounded="false" height="100%">
      <v-btn icon dark x-large @click="goBack()">
        <v-icon>mdi-chevron-left</v-icon>
      </v-btn>
      <v-row align="end">
        <v-col align-self="start" cols="4" style="padding:0;">
          <v-avatar class="profile" color="grey" size="80">
            <v-img :src="account.userinfo.avatar"></v-img>
          </v-avatar>
        </v-col>
        <v-col style="text-align:left;padding:0;">
          <v-list-item color="rgba(0, 0, 0, .4)" dark>
            <v-list-item-content>
              <v-list-item-title class="title">
                {{account.userinfo.username}}
                <v-icon :color="isVip" size="20">mdi-diamond-stone</v-icon>
              </v-list-item-title>
              <v-list-item-subtitle>个人中心</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-col>
      </v-row>
      <v-card-text>
        <v-list dark style="background:transparent">
          <v-list-item-title style="color:#fff;">开发中...</v-list-item-title>
          <v-list-item style="color:#fff;">历史购买记录，中奖记录</v-list-item>
          <v-list-item style="color:#fff;">通知管理，中奖提醒</v-list-item>
          <v-list-item style="color:#fff;">收支分析</v-list-item>
          <v-list-item style="color:#fff;">中意号码管理</v-list-item>
          <v-list-item style="color:#fff;">推荐好友、分享二维码</v-list-item>
          <v-list-item style="color:#fff;">系统更新</v-list-item>
          <v-list-item style="color:#fff;">开通高级功能</v-list-item>
          <v-list-item style="color:#fff;">捐助本项目</v-list-item>
        </v-list>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
import { mapState, mapMutations, mapGetters } from 'vuex'
import types from '@store/types'
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
      account: (state) => state.account,
    }),
    isVip() {
      console.log(this.account.userinfo)
      return this.account.userinfo.vip
        ? 'yellow accent-3'
        : 'blue-grey lighten-4'
    },
  },
  methods: {
    ...mapMutations({
      hiddenTopNav: types.nav.HIDE_TOP,
      hiddenBotNav: types.nav.HIDE_BOT,
      saveUserInfo: types.account.SAVE_USERINFO,
    }),
    goBack() {
      if (window.history.length) {
        this.$router.go(-1)
      } else {
        this.$router.push('/main/home')
      }
    },
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