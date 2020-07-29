<template>
  <div class="app-nav">
    <v-app-bar
      absolute
      color="teal lighten-3"
      dark
      shrink-on-scroll
      dense
      scroll-target="#scrolling-techniques"
      v-show="nav.showTop"
    >
      <v-app-bar-nav-icon></v-app-bar-nav-icon>

      <v-toolbar-title>{{lotteryConfig.lottery[lotteryConfig.type].name}}</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn icon @click="showNumbers=true">
        <v-badge
          overlap
          left
          color="error"
          :content="favorite.list.length"
          offset-x="8px"
          offset-y="9px"
          :value="favorite.list.length"
        >
          <v-icon>mdi-heart-multiple</v-icon>
        </v-badge>
      </v-btn>

      <v-btn icon @click="toggleType">
        <v-icon>{{lotteryConfig.lottery[lotteryConfig.type].icon}}</v-icon>
      </v-btn>
      <v-btn
        icon
        @click.stop="showDialog"
        v-show="nav.currentPage==='Home' || nav.currentPage ==='Analysis' || nav.currentPage ==='Map'"
      >
        <v-icon>mdi-cog</v-icon>
      </v-btn>
      <v-avatar color @click="showLoginPage()">
        <v-icon>mdi-account-circle</v-icon>
      </v-avatar>
    </v-app-bar>

    <div class="dialogs">
      <v-dialog v-model="lotteryConfig.dialog" max-width="290">
        <v-card class="mx-auto" max-width="400">
          <v-card-title class="headline">选号设置</v-card-title>
          <v-card-subtitle class="pb-0">可以修改生成数量和模式</v-card-subtitle>
          <v-list-item three-line>
            <v-list-item-content>
              <v-list-item-title>生成数量</v-list-item-title>
              <v-slider
                v-model="lotteryConfig.index"
                :max="4"
                :tick-labels="lotteryConfig.nums"
                class="mx-4"
                ticks
              ></v-slider>
            </v-list-item-content>
          </v-list-item>
          <v-list-item two-line v-show="lotteryConfig.nums[lotteryConfig.index]=='...'">
            <v-text-field label="自定义数量" v-model="lotteryConfig.customNum"></v-text-field>
          </v-list-item>

          <v-list-item three-line>
            <v-list-item-content>
              <v-list-item-title>模式</v-list-item-title>
              <v-radio-group v-model="lotteryConfig.mod">
                <v-radio label="混合" value="mix"></v-radio>
                <v-radio label="最少出现(默认)" value="min"></v-radio>
                <v-radio label="最多出现" value="max"></v-radio>
              </v-radio-group>
            </v-list-item-content>
          </v-list-item>

          <v-card-actions style="text-align:right;">
            <v-btn text @click="confirmLottery">确定</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-dialog v-model="chartConfig.dialog" max-width="290">
        <v-card class="mx-auto" max-width="400">
          <v-card-title class="headline">分析设置</v-card-title>
          <v-card-text>
            <v-radio-group v-model="chartConfig.chartmod">
              <v-radio label="号码出现次数分析" value="cnt"></v-radio>
              <v-radio label="冷热号" value="ch"></v-radio>
              <v-radio label="..." value="..."></v-radio>
            </v-radio-group>
          </v-card-text>
          <v-card-actions style="text-align:right;">
            <v-btn text @click="confirmChart">确定</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-dialog v-model="mapconfig.dialog" max-width="290">
        <v-card class="mx-auto" max-width="400">
          <v-card-title class="headline">地图搜索设置</v-card-title>
          <v-card-text>
            <v-text-field v-model="mapconfig.mapSearchRange" label="搜索范围"></v-text-field>
          </v-card-text>
          <v-card-actions style="text-align:right;">
            <v-btn text @click="confirmMap">确定</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-dialog v-model="showNumbers" max-width="100%">
        <v-card class="mx-auto" max-width="100%">
          <v-card-title class="headline">已选号码</v-card-title>
          <v-btn icon absolute top right @click="showNumbers = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-card-text>
            <v-list>
              <v-list-item v-for="(item,idx) in favorite.list" :key="idx">
                <v-list-item-content>
                  <v-row style="margin:0">
                    <v-col
                      style="padding:0;max-width:14%;line-height:44px;"
                      v-for="n in 6"
                      :key="n"
                    >
                      <v-btn fab dark x-small color="red">
                        <v-icon dark>{{item[n-1]}}</v-icon>
                      </v-btn>
                    </v-col>
                    <v-col style="padding:0;max-width:14%;line-height:44px;">
                      <v-btn fab dark x-small color="blue">
                        <v-icon dark>{{item[6]}}</v-icon>
                      </v-btn>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-card-text>
          <v-card-actions>
            <v-btn text @click="clearNumbers">清空</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </div>
</template>


<script>
import { mapState, mapMutations, mapGetters } from 'vuex'
import types from '@store/types'
export default {
  data() {
    return {
      mapSearchRange: 0,
      chartmod: '',
      showNumbers: false,
      lotteryConfig: {
        dialog: false,
        nums: [5, 10, 15, 20, '...'],
        customNum: 0,
        index: 1,
        mod: 'min',
        type: 'ssq',
        lottery: {
          ssq: {
            name: '双色球',
            icon: 'mdi-alpha-s-circle',
          },
          dlt: {
            name: '大乐透',
            icon: 'mdi-alpha-d-circle-outline',
          },
        },
      },
      confirmLotteryConifg: {
        mod: 'min',
        type: 'ssq',
        num: '10',
      },
      chartConfig: {
        dialog: false,
        chartmod: 'cnt',
      },
      mapconfig: {
        dialog: false,
        mapSearchRange: 1500,
      },
    }
  },
  computed: {
    ...mapState({
      lottery: (state) => state.lottery,
    }),
    ...mapState({
      map: (state) => state.map,
    }),
    ...mapState({
      history: (state) => state.history,
    }),
    ...mapState({
      favorite: (state) => state.favorite,
    }),
    ...mapState({
      nav: (state) => state.nav,
    }),
    ...mapState({
      account: (state) => state.account,
    }),
    ...mapState({
      chart: (state) => state.chart,
    }),
  },
  mounted() {
    this.initDefaultData()
  },
  methods: {
    logined() {
      if (this.account) {
        return 'grey light-2'
      } else {
        return ''
      }
    },
    confirmLottery() {
      this.lotteryConfig.dialog = false
      this.confirmLotteryConifg.num = this.lotteryConfig.customNum =
        this.lotteryConfig.index <= 3
          ? this.lotteryConfig.nums[this.lotteryConfig.index]
          : this.lotteryConfig.customNum
      this.confirmLotteryConifg.mod = this.lotteryConfig.mod
      this.setLotteryConfig(this.confirmLotteryConifg)
    },
    confirmChart() {
      this.chartConfig.dialog = false
      this.chartmod = this.chartConfig.chartmod
    },
    confirmMap() {
      this.mapconfig.dialog = false
      this.mapSearchRange = this.mapconfig.mapSearchRange
    },
    toggleType() {
      if (this.lotteryConfig.type === 'ssq') {
        this.confirmLotteryConifg.type = this.lotteryConfig.type = 'dlt'
      } else {
        this.confirmLotteryConifg.type = this.lotteryConfig.type = 'ssq'
      }
      this.setLotteryConfig(this.confirmLotteryConifg)
    },
    showDialog() {
      if (this.nav.currentPage === 'Home') {
        this.lotteryConfig.dialog = true
      } else if (this.nav.currentPage === 'Analysis') {
        this.chartConfig.dialog = true
      } else if (this.nav.currentPage === 'Map') {
        this.mapconfig.dialog = true
      }
    },
    clearNumbers() {
      this.clearFavorite()
      this.showNumbers = false
    },
    showLoginPage() {
      if (true) {
        this.$router.push('/login')
      } else {
      }
      this.bottomNav = 'account'
    },
    ...mapMutations({
      setLotteryConfig: types.lottery.SET_CONFIG,
      setHistoryLimit: types.history.SET_LIMIT,
      setHistoryFrom: types.history.SET_FROM,
      setHistoryTo: types.history.SET_TO,
      setChartMod: types.chart.SET_MOD,
      setMapRange: types.map.SET_RANGE,
      pushFavorite: types.favorite.PUSH,
      removeFavorite: types.favorite.RMOVE,
      clearFavorite: types.favorite.CLEAR,
      setCurrentPage: types.nav.CHOOSE_PAGE,
    }),
    initDefaultData() {
      this.setLotteryConfig(this.confirmLotteryConifg)
      this.setChartMod(this.chartmod)
      this.setMapRange(this.map)
      this.setCurrentPage('Home')
    },
  },
}
</script>


